package net.juniper.netconf.core;

import net.juniper.netconf.core.enums.DefaultOperationEnums;
import net.juniper.netconf.core.enums.ErrorOptionEnums;
import net.juniper.netconf.core.enums.TargetEnums;
import net.juniper.netconf.core.enums.TestOptionEnums;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * NETCONF相关的基本RPC操作
 *
 * @author zifangsky
 * @date 21/2/7
 * @since 1.0.0
 */
public interface RpcManager {
    /**
     * 返回与 Netconf 是否还保持连接状态
     * @return return true if connected
     */
    boolean isConnected();

    /**
     * 从Netconf session获取 sessionId
     *
     * @return Session ID
     */
    String getSessionId();

    /**
     * 发起一个RPC请求
     *
     * @param rpcContent RPC content
     * @return RPC reply sent by Netconf server
     * @throws IOException If there are errors communicating with the netconf server.
     */
    String executeRpc(String rpcContent) throws IOException;

    /**
     * 发起一个RPC请求
     *
     * @param rpcContent RPC content
     * @return RPC reply sent by Netconf server as a BufferedReader. This is
     * useful if we want continuous stream of output, rather than wait
     * for whole output till rpc execution completes.
     * @throws IOException if there are errors communicating with the Netconf server.
     */
    BufferedReader executeRpcRunning(String rpcContent) throws IOException;

    /**
     * 执行一个&#60;cli&#62;命令
     *
     * @param command the cli command to be executed.
     * @return result of the command, as a String.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    String runCliCommand(String command) throws IOException;

    /**
     * 执行一个&#60;cli&#62;命令
     *
     * @param command the cli command to be executed.
     *
     * @return result of the command, as a BufferedReader. This is
     * useful if we want continuous stream of output, rather than wait
     * for whole output till command execution completes.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    BufferedReader runCliCommandRunning(String command) throws IOException;

    /**
     * 执行&#60;get-config&#62;请求
     *
     * @param source source节点
     * @param filterTree filter正文部分
     * @return result of the command, as a String.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    String getConfig(TargetEnums source, String filterTree) throws IOException;

    /**
     * 获取 candidate 配置
     *
     * @param filterTree filter正文部分
     * @return configuration data as a string.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    String getCandidateConfig(String filterTree) throws IOException;

    /**
     * 获取 running 配置
     *
     * @param filterTree filter正文部分
     * @return configuration data as a string.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    String getRunningConfig(String filterTree) throws IOException;

    /**
     * 执行&#60;edit-config&#62;命令，默认source为running
     *
     * @param configTree config正文部分
     * @return result of the command, as a String.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    String editRunningConfig(String configTree) throws IOException;

    /**
     * 执行&#60;edit-config&#62;命令
     *
     * @param source source节点
     * @param configTree config正文部分
     * @return result of the command, as a String.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    String editConfig(TargetEnums source, String configTree) throws IOException;

    /**
     * 执行&#60;edit-config&#62;命令
     *
     * @param source source节点
     * @param defaultOperation default-operation节点
     * @param testOption test-option节点
     * @param errorOption error-option节点
     * @param configTree config正文部分
     * @return result of the command, as a String.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    String editConfig(TargetEnums source, DefaultOperationEnums defaultOperation,
                      TestOptionEnums testOption, ErrorOptionEnums errorOption, String configTree) throws IOException;

    /**
     * 加锁
     *
     * @return true if successful.
     * @throws IOException If there are issues communicating with the netconf server.
     */
    boolean lock() throws IOException;

    /**
     * 加锁
     *
     * @param source source节点
     * @return true if successful.
     * @throws IOException If there are issues communicating with the netconf server.
     */
    boolean lock(TargetEnums source) throws IOException;

    /**
     * 解锁
     *
     * @return true if successful.
     * @throws IOException If there are issues communicating with the netconf server.
     */
    boolean unlock() throws IOException;

    /**
     * 解锁
     *
     * @param source source节点
     * @return true if successful.
     * @throws IOException If there are issues communicating with the netconf server.
     */
    boolean unlock(TargetEnums source) throws IOException;

    /**
     * 提交
     *
     * @return true if successful.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean commit() throws IOException;

    /**
     * 确定并提交
     *
     * @return true if successful.
     * @param confirmTimeout Timeout period for confirmed commit, in seconds. If unspecified, the confirm timeout defaults to 600 seconds.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean commitConfirm(int confirmTimeout) throws IOException;

    /**
     * 验证
     *
     * @return true if the device was able to satisfy the request.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean validate() throws IOException;

    /**
     * 优雅地关闭 NETCONF 会话
     * <p>When a NETCONF server receives a <close-session> request, it will
     *       gracefully close the session.  The server will release any locks
     *       and resources associated with the session and gracefully close any
     *       associated connections.  Any NETCONF requests received after a
     *       <close-session> request will be ignored.
     * </p>
     *
     * @return true if successful.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean closeSession() throws IOException;

    /**
     * 强制关闭 NETCONF 会话
     * <p>When a NETCONF entity receives a <kill-session> request for an
     *       open session, it will abort any operations currently in process,
     *       release any locks and resources associated with the session, and
     *       close any associated connections.
     * </p>
     * <p>If a NETCONF server receives a <kill-session> request while
     *       processing a confirmed commit (Section 8.4), it MUST restore the
     *       configuration to its state before the confirmed commit was issued.
     * </p>
     * <p>Otherwise, the <kill-session> operation does not roll back
     *       configuration or other device state modifications made by the
     *       entity holding the lock.
     * </p>
     *
     * @param sessionId Session identifier of the NETCONF session to be terminated.
     *                  If this value is equal to the current session ID, an "invalid-value" error is returned.
     * @return true if successful.
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean killSession(int sessionId) throws IOException;
}
