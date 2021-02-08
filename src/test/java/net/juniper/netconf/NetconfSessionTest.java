package net.juniper.netconf;

import com.jcraft.jsch.Channel;
import lombok.extern.slf4j.Slf4j;
import net.juniper.netconf.core.NetconfSession;
import net.juniper.netconf.core.exception.NetconfException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@Slf4j
@Category(Test.class)
public class NetconfSessionTest {

    public static final int CONNECTION_TIMEOUT = 2000;
    public static final int COMMAND_TIMEOUT = 5000;

    private static final String FAKE_HELLO = "fake hello";
    private static final String MERGE_LOAD_TYPE = "merge";
    private static final String REPLACE_LOAD_TYPE = "replace";
    private static final String BAD_LOAD_TYPE = "other";
    private static final String FAKE_TEXT_FILE_PATH = "fakepath";

    private static final String DEVICE_PROMPT = "]]>]]>";
    private static final byte[] DEVICE_PROMPT_BYTE = DEVICE_PROMPT.getBytes();
    private static final String FAKE_RPC_REPLY = "<rpc>fakedata</rpc>";
    private static final String NETCONF_SYNTAX_ERROR_MSG_FROM_DEVICE = "netconf error: syntax error";

    @Mock
    private NetconfSession mockNetconfSession;
    @Mock
    private DocumentBuilder builder;
    @Mock
    private Channel mockChannel;

    private BufferedOutputStream out;
    private PipedOutputStream outPipe;
    private PipedInputStream inPipe;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        inPipe = new PipedInputStream(8096);
        outPipe = new PipedOutputStream(inPipe);
        PipedInputStream pipeInput = new PipedInputStream(1024);
        out = new BufferedOutputStream(new PipedOutputStream(pipeInput));

        when(mockChannel.getInputStream()).thenReturn(inPipe);
        when(mockChannel.getOutputStream()).thenReturn(out);
    }

    @Test
    public void GIVEN_createSession_WHEN_timeoutExceeded_THEN_throwSocketTimeoutException() throws Exception {
        Thread thread = new Thread(() -> {
            try {
                outPipe.write(FAKE_RPC_REPLY.getBytes());
                for (int i = 0; i < 7; i++) {
                    outPipe.write(FAKE_RPC_REPLY.getBytes());
                    Thread.sleep(200);
                    outPipe.flush();
                }
                Thread.sleep(200);
                outPipe.close();
            } catch (IOException | InterruptedException e) {
                log.error("error =", e);
            }
        });
        thread.start();

        assertThatThrownBy(() -> createNetconfSession(1000))
                .isInstanceOf(SocketTimeoutException.class)
                .hasMessage("Command timeout limit was exceeded: 1000");
    }

    @Test
    public void GIVEN_createSession_WHEN_connectionClose_THEN_throwSocketTimeoutException() throws Exception {
        Thread thread = new Thread(() -> {
            try {
                outPipe.write(FAKE_RPC_REPLY.getBytes());
                Thread.sleep(200);
                outPipe.flush();
                Thread.sleep(200);
                outPipe.close();
            } catch (IOException | InterruptedException e) {
                log.error("error =", e);
            }
        });
        thread.start();

        assertThatThrownBy(() -> createNetconfSession(COMMAND_TIMEOUT))
                .isInstanceOf(NetconfException.class)
                .hasMessage("Input Stream has been closed during reading.");
    }

    @Test
    public void GIVEN_createSession_WHEN_devicePromptWithoutLF_THEN_correctResponse() throws Exception {
        when(mockChannel.getInputStream()).thenReturn(inPipe);
        when(mockChannel.getOutputStream()).thenReturn(out);

        Thread thread = new Thread(() -> {
            try {
                outPipe.write(FAKE_RPC_REPLY.getBytes());
                outPipe.write(DEVICE_PROMPT_BYTE);
                Thread.sleep(200);
                outPipe.flush();
                Thread.sleep(200);
                outPipe.close();
            } catch (IOException | InterruptedException e) {
                log.error("error =", e);
            }
        });
        thread.start();

        createNetconfSession(COMMAND_TIMEOUT);
    }

    private NetconfSession createNetconfSession(int commandTimeout) throws IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new NetconfException(String.format("Error creating XML Parser: %s", e.getMessage()));
        }

        return new NetconfSession(mockChannel, CONNECTION_TIMEOUT, commandTimeout, new ArrayList<>());
    }
}
