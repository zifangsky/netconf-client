/*
 Copyright (c) 2013 Juniper Networks, Inc.
 All Rights Reserved

 Use is subject to license terms.
*/

package cn.zifangsky.netconf.core;

import cn.zifangsky.netconf.core.exception.NetconfException;
import com.jcraft.jsch.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * A <code>Device</code> is used to define a Netconf server.
 * <p>
 * A new device is created using the Device.Builder.build()
 * <p>
 * Example:
 * <pre>
 * {@code}
 * Device device = Device.builder().hostName("hostname")
 *     .userName("username")
 *     .password("password")
 *     .hostKeysFileName("hostKeysFileName")
 *     .build();
 * </pre>
 * <ol>
 * <li>creates a {@link DefaultDevice} object.</li>
 * <li>perform netconf operations on the Device object.</li>
 * <li>If needed, call the method createNetconfSession() to create another
 * NetconfSession.</li>
 * <li>Finally, one must close the Device and release resources with the
 * {@link #close() close()} method.</li>
 * </ol>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.1.0
 */
@Slf4j
@Getter
@ToString
public class DefaultDevice implements Device, AutoCloseable {

    private static final int DEFAULT_NETCONF_PORT = 830;
    private static final int DEFAULT_TIMEOUT = 5000;

    private final JSch sshClient;
    private final String hostName;
    private final int port;
    private final int connectionTimeout;
    private final int commandTimeout;

    private final String userName;
    private final String password;

    private final boolean keyBasedAuthentication;
    private final String pemKeyFile;

    private final boolean strictHostKeyChecking;
    private final String hostKeysFileName;

    private final List<String> netConfCapabilities;
    /**
     * 跟这个设备相关的其他额外信息
     */
    private Map<String, Object> extraMsgMap;

    private ChannelSubsystem sshChannel;
    private Session sshSession;
    private NetconfSession netconfSession;

    @Builder
    public DefaultDevice(JSch sshClient,
                         @NonNull String hostName,
                         Integer port,
                         Integer timeout,
                         Integer connectionTimeout,
                         Integer commandTimeout,
                         @NonNull String userName,
                         String password,
                         Boolean keyBasedAuthentication,
                         String pemKeyFile,
                         Boolean strictHostKeyChecking,
                         String hostKeysFileName,
                         List<String> netConfCapabilities,
                         Map<String, Object> extraMsgMap
    ) throws NetconfException {
        this.hostName = hostName;
        this.port = (port != null) ? port : DEFAULT_NETCONF_PORT;
        int commonTimeout = (timeout != null) ? timeout : DEFAULT_TIMEOUT;
        this.connectionTimeout = (connectionTimeout != null) ? connectionTimeout : commonTimeout;
        this.commandTimeout = (commandTimeout != null) ? commandTimeout : commonTimeout;

        this.userName = userName;
        this.password = password;

        if (this.password == null && pemKeyFile == null) {
            throw new NetconfException("Auth requires either setting the password or the pemKeyFile");
        }

        this.keyBasedAuthentication = (keyBasedAuthentication != null) ? keyBasedAuthentication : false;
        this.pemKeyFile = pemKeyFile;

        if (this.keyBasedAuthentication && pemKeyFile == null) {
            throw new NetconfException("key based authentication requires setting the pemKeyFile");
        }

        this.strictHostKeyChecking = (strictHostKeyChecking != null) ? strictHostKeyChecking : true;
        this.hostKeysFileName = hostKeysFileName;

        if (this.strictHostKeyChecking && hostKeysFileName == null) {
            throw new NetconfException("Strict Host Key checking requires setting the hostKeysFileName");
        }

        this.netConfCapabilities = (netConfCapabilities != null) ? netConfCapabilities : this.applyDefaultClientCapabilitiesIfNecessary();
        this.sshClient = (sshClient != null) ? sshClient : new JSch();

        this.extraMsgMap = (extraMsgMap == null ? new HashMap<>(4) : extraMsgMap);
    }


    /**
     *  创建一个 Netconf session
     *
     * @throws NetconfException if there are issues communicating with the Netconf server.
     */
    @Override
    public void connect() throws NetconfException {
        if (hostName == null || userName == null) {
            throw new NetconfException("Login parameters of Device can't be null.");
        }
        if (password == null && pemKeyFile == null) {
            throw new NetconfException("Login parameters of Device can't be null.");
        }

        this.netconfSession = this.createNetconfSession();
    }

    /**
     * 获取当前 sessionId
     *
     * @return Session ID
     */
    @Override
    public String getSessionId() {
        this.checkNetConfSessionEstablished();
        return this.netconfSession.getSessionId();
    }

    /**
     * 从Netconf session获取capability
     *
     * @return server capability
     */
    @Override
    public List<String> getServerCapability() {
        this.checkNetConfSessionEstablished();
        return this.netconfSession.getServerCapability();
    }

    /**
     * 返回与 Netconf 是否还保持连接状态
     */
    @Override
    public boolean isConnected() {
        return (isChannelConnected() && isSessionConnected());
    }

    /**
     * 返回最后一个返回报文
     *
     * @return Last RPC reply, as a string.
     */
    @Override
    public String getLastRpcReply() {
        this.checkNetConfSessionEstablished();
        return this.netconfSession.getLastRpcReply();
    }

    /**
     * 检查最后一个返回报文是否执行成功
     */
    @Override
    public boolean lastReplyIsSuccess() {
        this.checkNetConfSessionEstablished();
        return this.netconfSession.lastReplyIsSuccess();
    }

    /**
     * 返回最后一个返回报文是否包含错误
     *
     * @return true if any errors are found in last RPC reply.
     */
    @Override
    public boolean lastReplyHasError() {
        this.checkNetConfSessionEstablished();
        return this.netconfSession.lastReplyHasError();
    }

    /**
     * 返回最后一个返回报文是否包含警告
     *
     * @return true if any warning are found in last RPC reply.
     */
    @Override
    public boolean lastReplyHasWarning() {
        this.checkNetConfSessionEstablished();
        return this.netconfSession.lastReplyHasWarning();
    }

    /**
     * 通过 shell 模式执行命令
     *
     * @param command The command to be executed in shell mode.
     * @return Result of the command execution, as a String.
     * @throws IOException if there are issues communicating with the Netconf server.
     */
    @Override
    public String runShellCommand(String command) throws IOException {
        if (!isConnected()) {
            return "Could not find open connection.";
        }

        try (BufferedReader bufferReader = this.runShellCommandRunning(command)) {
            StringBuilder reply = new StringBuilder();
            while (true) {
                String line;
                try {
                    line = bufferReader.readLine();
                } catch (Exception e) {
                    throw new NetconfException(e.getMessage());
                }
                if (line == null || line.equals(NetconfConstants.EMPTY_LINE)) {
                    break;
                }
                reply.append(line).append(NetconfConstants.LF);
            }
            return reply.toString();
        }
    }

    /**
     * 通过 shell 模式执行命令
     *
     * @param command The command to be executed in shell mode.
     * @return Result of the command execution, as a BufferedReader. This is
     * useful if we want continuous stream of output, rather than wait
     * for whole output till command execution completes.
     * @throws IOException if there are issues communicating with the Netconf server.
     */
    @Override
    public BufferedReader runShellCommandRunning(String command)
            throws IOException {
        if (!isConnected()) {
            throw new IOException("Could not find open connection");
        }
        ChannelExec channel;
        try {
            channel = (ChannelExec) sshSession.openChannel("exec");
        } catch (JSchException e) {
            throw new NetconfException(String.format("Failed to open exec session: %s", e.getMessage()));
        }
        channel.setCommand(command);
        InputStream stdout = channel.getInputStream();

        return new BufferedReader(new InputStreamReader(stdout, Charset.defaultCharset()));
    }

    /**
     * 发起一个RPC请求
     *
     * @param rpcContent RPC content
     * @return RPC reply sent by Netconf server
     * @throws IOException      If there are errors communicating with the netconf server.
     */
    @Override
    public String executeRpc(String rpcContent) throws IOException {
        this.checkNetConfSessionEstablished();
        return this.netconfSession.executeRpc(rpcContent);
    }

    /**
     * 发起一个RPC请求
     *
     * @param rpcContent RPC content
     * @return RPC reply sent by Netconf server as a BufferedReader. This is
     * useful if we want continuous stream of output, rather than wait
     * for whole output till rpc execution completes.
     * @throws IOException if there are errors communicating with the Netconf server.
     */
    @Override
    public BufferedReader executeRpcRunning(String rpcContent) throws IOException {
        this.checkNetConfSessionEstablished();
        return this.netconfSession.executeRpcRunning(rpcContent);
    }

    /**
     * 关闭 Netconf 连接
     */
    @Override
    public void close() {
        this.doClose();
    }

    @Override
    public void doClose() {
        if (isChannelConnected()) {
            sshChannel.disconnect();
        }
        if (isSessionConnected()) {
            sshSession.disconnect();
        }
    }

    /**
     * 获取设置的额外参数
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> getExtraMsgMap() {
        return Collections.unmodifiableMap(this.extraMsgMap);
    }

    /**
     * 设置默认的客户端 capabilities
     * <p>https://tools.ietf.org/html/rfc6241#section-8</p>
     *
     * @return List of default client capabilities.
     */
    protected List<String> applyDefaultClientCapabilitiesIfNecessary() {
        List<String> defaultCap = new ArrayList<>();

        defaultCap.add(NetconfConstants.URN_IETF_PARAMS_NETCONF_BASE_1_0);
        defaultCap.add(NetconfConstants.URN_IETF_PARAMS_NETCONF_BASE_1_0 + "#candidate");
        defaultCap.add(NetconfConstants.URN_IETF_PARAMS_NETCONF_BASE_1_0 + "#confirmed-commit");
        defaultCap.add(NetconfConstants.URN_IETF_PARAMS_NETCONF_BASE_1_0 + "#validate");
        defaultCap.add(NetconfConstants.URN_IETF_PARAMS_NETCONF_BASE_1_0 + "#url?protocol=http,ftp,file");
        return defaultCap;
    }

    /**
     * 创建一个 Netconf session
     *
     * @return NetconfSession
     * @throws NetconfException if there are issues communicating with the Netconf server.
     */
    private NetconfSession createNetconfSession() throws NetconfException {
        if (!isConnected()) {
            try {
                if (strictHostKeyChecking) {
                    if (hostKeysFileName == null) {
                        throw new NetconfException("Cannot do strictHostKeyChecking if hostKeysFileName is null");
                    }
                    sshClient.setKnownHosts(hostKeysFileName);
                }
            } catch (JSchException e) {
                throw new NetconfException(String.format("Error loading known hosts file: %s", e.getMessage()));
            }

            sshClient.setHostKeyRepository(sshClient.getHostKeyRepository());
            log.info("Connecting to host {} on port {}.", hostName, port);

            //1. ssh连接
            if (keyBasedAuthentication) {
                loadPrivateKey();
                sshSession = loginWithPrivateKey(connectionTimeout);
            } else {
                sshSession = loginWithUserPass(connectionTimeout);
            }

            try {
                sshSession.setTimeout(connectionTimeout);
            } catch (JSchException e) {
                throw new NetconfException(String.format("Error setting session timeout: %s", e.getMessage()));
            }
            if (sshSession.isConnected()) {
                log.info("Connected to host {} - Timeout set to {} msecs.", hostName, sshSession.getTimeout());
            } else {
                throw new NetconfException("Failed to connect to host. Unknown reason");
            }
        }

        //2. 打开「subsystem」channel
        try {
            sshChannel = (ChannelSubsystem) sshSession.openChannel("subsystem");
            sshChannel.setSubsystem("netconf");
            return new NetconfSession(sshChannel, connectionTimeout, commandTimeout, this.netConfCapabilities);
        } catch (JSchException | IOException e) {
            throw new NetconfException("Failed to create Netconf session:", e);
        }
    }

    /**
     * 使用「用户名+密码」连接ssh
     */
    private Session loginWithUserPass(int timeoutMilliSeconds) throws NetconfException {
        try {
            Session session = sshClient.getSession(userName, hostName, port);
            session.setConfig("userauth", "password");
            session.setConfig("StrictHostKeyChecking", isStrictHostKeyChecking() ? "yes" : "no");
            session.setPassword(password);
            session.connect(timeoutMilliSeconds);
            return session;
        } catch (JSchException e) {
            throw new NetconfException(String.format("Error connecting to host: %s - Error: %s",
                    hostName, e.getMessage()));
        }
    }

    /**
     * 使用「私钥」连接ssh
     */
    private Session loginWithPrivateKey(int timeoutMilliSeconds) throws NetconfException {
        try {
            Session session = sshClient.getSession(userName, hostName, port);
            session.setConfig("userauth", "publickey");
            session.setConfig("StrictHostKeyChecking", isStrictHostKeyChecking() ? "yes" : "no");
            session.connect(timeoutMilliSeconds);
            return session;
        } catch (JSchException e) {
            throw new NetconfException(String.format("Error using key pair file: %s to connect to host: %s",
                    pemKeyFile, hostName), e);
        }
    }

    /**
     * 加载「私钥」
     */
    private void loadPrivateKey() throws NetconfException {
        try {
            sshClient.addIdentity(pemKeyFile);
        } catch (JSchException e) {
            throw new NetconfException(String.format("Error parsing the pemKeyFile: %s", e.getMessage()));
        }
    }

    private boolean isChannelConnected() {
        if (sshChannel == null) {
            return false;
        }
        return sshChannel.isConnected();
    }

    private boolean isSessionConnected() {
        if (sshSession == null) {
            return false;
        }
        return sshSession.isConnected();
    }

    /**
     * 检查 NETCONF 会话是否已经建立
     */
    private void checkNetConfSessionEstablished(){
        if (netconfSession == null) {
            throw new IllegalStateException("Cannot execute RPC, you need to establish a connection first.");
        }
    }

}