package cn.zifangsky.netconf.core;

import cn.zifangsky.netconf.core.exception.NetconfException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * A <code>Device</code> is used to define a Netconf server.
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.1.0
 */
public interface Device {
    /**
     *  创建一个 Netconf session
     *
     * @throws NetconfException if there are issues communicating with the Netconf server.
     */
    void connect() throws NetconfException;

    /**
     * 获取当前 sessionId
     *
     * @return Session ID
     */
    String getSessionId();

    /**
     * 从Netconf session获取capability
     *
     * @return server capability
     */
    List<String> getServerCapability();

    /**
     * 返回与 Netconf 是否还保持连接状态
     */
    boolean isConnected();

    /**
     * 返回最后一个返回报文
     *
     * @return Last RPC reply, as a string.
     */
    String getLastRpcReply();

    /**
     * 检查最后一个返回报文是否执行成功
     */
    boolean lastReplyIsSuccess();

    /**
     * 返回最后一个返回报文是否包含错误
     *
     * @return true if any errors are found in last RPC reply.
     */
    boolean lastReplyHasError();

    /**
     * 返回最后一个返回报文是否包含警告
     *
     * @return true if any warning are found in last RPC reply.
     */
    boolean lastReplyHasWarning();

    /**
     * 通过 shell 模式执行命令
     *
     * @param command The command to be executed in shell mode.
     * @return Result of the command execution, as a String.
     * @throws IOException if there are issues communicating with the Netconf server.
     */
    String runShellCommand(String command) throws IOException;

    /**
     * 通过 shell 模式执行命令
     *
     * @param command The command to be executed in shell mode.
     * @return Result of the command execution, as a BufferedReader. This is
     * useful if we want continuous stream of output, rather than wait
     * for whole output till command execution completes.
     * @throws IOException if there are issues communicating with the Netconf server.
     */
    BufferedReader runShellCommandRunning(String command) throws IOException;

    /**
     * 发起一个RPC请求
     *
     * @param rpcContent RPC content
     * @return RPC reply sent by Netconf server
     * @throws java.io.IOException      If there are errors communicating with the netconf server.
     */
    String executeRpc(String rpcContent) throws IOException;

    /**
     * 发起一个RPC请求
     *
     * @param rpcContent RPC content
     * @return RPC reply sent by Netconf server as a BufferedReader. This is
     * useful if we want continuous stream of output, rather than wait
     * for whole output till rpc execution completes.
     * @throws java.io.IOException if there are errors communicating with the Netconf server.
     */
    BufferedReader executeRpcRunning(String rpcContent) throws IOException;

    /**
     * 关闭 Netconf 连接
     */
    void doClose();
}