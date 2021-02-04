package net.juniper.netconf;

import net.juniper.netconf.core.Device;
import net.juniper.netconf.core.NetconfException;
import net.juniper.netconf.core.XML;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertFalse;


@Category(Test.class)
public class DeviceTest {

    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private Device createTestDevice() throws NetconfException {
        return Device.builder()
                .hostName(TEST_HOSTNAME)
                .userName(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .port(DEFAULT_NETCONF_PORT)
                .strictHostKeyChecking(false)
                .build();
    }

    /**
     * 测试连接情况
     */
    @Test
    public void GIVEN_requiredParameters_THEN_createDevice() throws NetconfException {
        Device device = createTestDevice();
        device.connect();
        System.out.println(device.getSessionId() + ": " + device.isConnected());
    }

    /**
     * 查看安全策略配置
     */
    @Test
    public void executeRPC() throws Exception {
        Device device = createTestDevice();
        device.connect();

        XML xml = device.executeRPC("<get-config><source><running/></source><filter type=\"subtree\"><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"></sec-policy></filter></get-config>");
        System.out.println(xml.toString());
    }



    @Test
    public void GIVEN_newDevice_WHEN_withNullUserName_THEN_throwsException() {
        assertThatThrownBy(() -> Device.builder().hostName("foo").build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("userName is marked @NonNull but is null");
    }

    @Test
    public void GIVEN_newDevice_WHEN_withHostName_THEN_throwsException() {
        assertThatThrownBy(() -> Device.builder().userName("foo").build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("hostName is marked @NonNull but is null");
    }

    @Test
    public void GIVEN_newDevice_WHEN_checkIfConnected_THEN_returnFalse() throws NetconfException {
        Device device = createTestDevice();
        assertFalse(device.isConnected());
    }
}
