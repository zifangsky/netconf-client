/*
 Copyright (c) 2013 Juniper Networks, Inc.
 All Rights Reserved

 Use is subject to license terms.

*/

package net.juniper.netconf.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Charsets;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.juniper.netconf.core.model.RpcReply;
import net.juniper.netconf.core.exception.NetconfException;
import net.juniper.netconf.core.model.HelloRpc;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A <code>NetconfSession</code> object is used to call the Netconf driver
 * methods.
 * This is derived by creating a Device first,
 * and calling createNetconfSession().
 * <p>
 * Typically, one
 * <ol>
 * <li>creates a Device object.</li>
 * <li>calls the createNetconfSession() method to get a NetconfSession
 * object.</li>
 * <li>perform operations on the NetconfSession object.</li>
 * </ol>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.1.0
 */
@Slf4j
public class NetconfSession {
    private static ThreadLocal<XmlMapper> XMLMAPPER_RESOURCES = ThreadLocal.withInitial(XmlMapper::new);

    /**
     * hello返回报文的标识
     */
    public static final String HELLO_RPC_REPLY_FLAG = "<hello";
    /**
     * 普通rpc返回报文的标识
     */
    public static final String COMMON_RPC_REPLY_FLAG = "<rpc-reply";
    /**
     * XML一个节点开始的标识
     */
    public static final String XML_START_FLAG = "<";
    /**
     * <rpc>报文的起始标识
     */
    public static final String RPC_START_FLAG = "<rpc>";
    /**
     * <rpc>报文为空的标识
     */
    public static final String RPC_NULL_TREE_FLAG = "<rpc/>";

    private InputStream stdInStreamFromDevice;
    private OutputStream stdOutStreamToDevice;
    /**
     * NETCONF Server返回的capabilities
     */
    private List<String> serverCapabilities;
    /**
     * sessionId
     */
    private String sessionId;
    /**
     * 命令执行超时时间
     */
    private final int commandTimeout;
    /**
     * 上一条命令的执行结果
     */
    private String lastRpcReply;
    /**
     * 上一条命令是否执行成功
     */
    private boolean lastReplyIsSuccess = true;
    /**
     * 上一条命令是否包含错误
     */
    private boolean lastReplyHasError;
    /**
     * 上一条命令是否包含错误
     */
    private boolean lastReplyHasWarning;
    /**
     * 手动指定<rpc>层级的属性
     */
    private final Map<String, String> rpcAttrMap = new ConcurrentHashMap<>();
    /**
     * 缓存<rpc>层级的属性（一旦缓存成功就不再更改）
     */
    private String rpcAttributes;
    /**
     * 消息ID
     */
    private AtomicInteger messageId = new AtomicInteger(0);
    /**
     * Bigger than inner buffer in BufferReader class
     */
    public static final int BUFFER_SIZE = 9 * 1024;

    public NetconfSession(Channel netConfChannel, int timeout, List<String> netconfCapabilities) throws IOException {
        this(netConfChannel, timeout, timeout, netconfCapabilities);
    }

    public NetconfSession(Channel netConfChannel, int connectionTimeout, int commandTimeout, List<String> netconfCapabilities) throws IOException {
        this.stdInStreamFromDevice = netConfChannel.getInputStream();
        this.stdOutStreamToDevice = netConfChannel.getOutputStream();
        try {
            netConfChannel.connect(connectionTimeout);
        } catch (JSchException e) {
            throw new NetconfException("Failed to create Netconf session:" +
                    e.getMessage());
        }
        this.commandTimeout = commandTimeout;

        //发送<hello>报文
        String helloRpc = createHelloRpc(netconfCapabilities);
        sendHello(helloRpc);
    }


    /**
     * 从Netconf session获取capability
     *
     * @return server capability
     */
    public List<String> getServerCapability() {
        return this.serverCapabilities;
    }

    /**
     * 获取当前 sessionId
     *
     * @return Session ID as a string.
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * 执行一个RPC命令
     *
     * @param rpcContent RPC content to be sent.
     * @return RPC reply sent by Netconf server as a BufferedReader. This is
     * useful if we want continuous stream of output, rather than wait
     * for whole output till command execution completes.
     * @throws java.io.IOException If there are issues communicating with the netconf server.
     */
    public BufferedReader executeRpcRunning(String rpcContent) throws IOException {
        return getRpcReplyRunning(this.wrapRpcTag(rpcContent));
    }

    /**
     * 发起一个RPC请求
     *
     * @param rpcContent RPC content to be sent. For example, to send an rpc
     *                   &lt;rpc&gt;&lt;get-chassis-inventory/&gt;&lt;/rpc&gt;, the
     *                   String to be passed can be
     *                      "&lt;get-chassis-inventory/&gt;" OR
     *                      "get-chassis-inventory" OR
     *                      "&lt;rpc&gt;&lt;get-chassis-inventory/&gt;&lt;/rpc&gt;"
     * @return RPC reply sent by Netconf server
     * @throws java.io.IOException      If there are issues communicating with the netconf server.
     */
    public String executeRpc(String rpcContent) throws IOException {
        String rpcReply = this.getRpcReply(this.wrapRpcTag(rpcContent));

        this.updateLastRpcReply(rpcReply);
        return rpcReply;
    }

    /**
     * 返回最后一个返回报文
     *
     * @return Last RPC reply, as a string.
     */
    public String getLastRpcReply() {
        return this.lastRpcReply;
    }

    /**
     * 检查最后一个返回报文是否执行成功
     */
    public boolean lastReplyIsSuccess() {
        if(!this.lastReplyIsSuccess){
            log.debug("The last request cannot be completed for any reason, lastRpcReply:{}", this.lastRpcReply);
        }

        return this.lastReplyIsSuccess;
    }

    /**
     * 返回最后一个返回报文是否包含错误
     *
     * @return true if any errors are found in last RPC reply.
     */
    public boolean lastReplyHasError() {
        return this.lastReplyHasError;
    }

    /**
     * 返回最后一个返回报文是否包含警告
     *
     * @return true if any warning are found in last RPC reply.
     */
    public boolean lastReplyHasWarning() {
        return this.lastReplyHasWarning;
    }

    /**
     * 发送hello报文
     */
    private void sendHello(String hello) throws IOException {
        String reply = this.getRpcReply(hello);
        this.updateLastRpcReply(reply);
    }

    /**
     * 执行一个rpc请求报文，返回{@link BufferedReader}
     */
    private BufferedReader getRpcReplyRunning(String rpc) throws IOException {
        this.sendRpcRequest(rpc);
        return new BufferedReader(new InputStreamReader(this.stdInStreamFromDevice, Charsets.UTF_8));
    }

    /**
     * 执行一个rpc请求报文
     */
    private String getRpcReply(String rpc) throws IOException {
        // write the rpc to the device
        this.sendRpcRequest(rpc);

        final char[] buffer = new char[BUFFER_SIZE];
        final StringBuilder rpcReply = new StringBuilder();
        final long startTime = System.nanoTime();
        final Reader in = new InputStreamReader(this.stdInStreamFromDevice, Charsets.UTF_8);

        boolean timeoutNotExceeded = true;
        int promptPosition = -1;
        while ((promptPosition = rpcReply.indexOf(NetconfConstants.DEVICE_PROMPT)) < 0 &&
                (timeoutNotExceeded = (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) < commandTimeout))) {
            int charsRead = in.read(buffer, 0, buffer.length);

            if (charsRead < 0) {
                throw new NetconfException("Input Stream has been closed during reading.");
            }
            rpcReply.append(buffer, 0, charsRead);
        }

        if (!timeoutNotExceeded) {
            throw new SocketTimeoutException("Command timeout limit was exceeded: " + commandTimeout);
        }

        // fixing the rpc reply by removing device prompt
        log.debug("Received Netconf RPC-Reply\n{}", rpcReply);
        rpcReply.setLength(promptPosition);

        return rpcReply.toString();
    }

    /**
     * 拼接 messageId 和 RpcAttributes，然后发送请求
     */
    private void sendRpcRequest(String rpc) throws IOException {
        // RFC conformance for XML type, namespaces and message ids for RPCs
        int newMessageId = messageId.incrementAndGet();

        rpc = rpc.replace("<rpc>", "<rpc" + this.getRpcAttributes() + " message-id=\"" + newMessageId + "\">").trim();
        if (!rpc.contains(NetconfConstants.XML_VERSION)) {
            rpc = NetconfConstants.XML_VERSION + rpc;
        }

        // writing the rpc to the device
        log.debug("Sending Netconf RPC\n{}", rpc);
        this.stdOutStreamToDevice.write(rpc.getBytes(Charsets.UTF_8));
        this.stdOutStreamToDevice.flush();
    }

    /**
     * 获取rpc层级的属性，如果没有手动指定，则设置一个默认的xmlns
     */
    private synchronized String getRpcAttributes() {
        if(this.rpcAttributes == null) {
            boolean useDefaultNamespace = true;
            StringBuilder attributes = new StringBuilder();

            for (Map.Entry<String, String> attribute : this.rpcAttrMap.entrySet()) {
                attributes.append(String.format(" %1s=\"%2s\"", attribute.getKey(), attribute.getValue()));

                if ("xmlns".equals(attribute.getKey())) {
                    useDefaultNamespace = false;
                }
            }

            if (useDefaultNamespace) {
                attributes.append(" xmlns=\"" + NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0 + "\"");
            }
            this.rpcAttributes = attributes.toString();
        }
        return this.rpcAttributes;
    }

    /**
     * 给RPC报文的正文部分包装一层rpc节点
     */
    private String wrapRpcTag(@NonNull String rpcContent) throws IllegalArgumentException {
        if (rpcContent == null) {
            throw new IllegalArgumentException("Null RPC");
        }
        rpcContent = rpcContent.trim();

        //如果不包含<rpc>，且rpcContent不是<rpc/>，则在外面包装一层<rpc>
        if (!rpcContent.startsWith(RPC_START_FLAG) && !RPC_NULL_TREE_FLAG.equals(rpcContent)) {
            if (rpcContent.startsWith(XML_START_FLAG)) {
                rpcContent = "<rpc>" + rpcContent + "</rpc>";
            } else {
                rpcContent = "<rpc>" + "<" + rpcContent + "/>" + "</rpc>";
            }
        }
        return rpcContent + NetconfConstants.DEVICE_PROMPT;
    }

    /**
     * 通过一系列的 netconf capabilities 创建hello报文，更多参考：https://tools.ietf.org/html/rfc6241#section-8.1
     *
     * @param capabilities A list of netconf capabilities
     * @return the hello RPC that represents those capabilities.
     */
    private String createHelloRpc(List<String> capabilities) {
        StringBuilder helloRpc = new StringBuilder();
        helloRpc.append("<hello xmlns=\"" + NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0 + "\">\n");
        helloRpc.append("<capabilities>\n");
        for (Object o : capabilities) {
            String capability = (String) o;
            helloRpc.append("<capability>")
                    .append(capability)
                    .append("</capability>\n");
        }
        helloRpc.append("</capabilities>\n");
        helloRpc.append("</hello>\n");
        helloRpc.append(NetconfConstants.DEVICE_PROMPT);
        return helloRpc.toString();
    }

    /**
     * 更新lastRpcReply
     */
    private synchronized void updateLastRpcReply(String xml) throws JsonProcessingException {
        if(StringUtils.isBlank(xml)){
            return;
        }
        this.lastRpcReply = xml;

        if(xml.contains(COMMON_RPC_REPLY_FLAG)){
            this.handleCommonRpcReply(xml);
        }
        else if(xml.contains(HELLO_RPC_REPLY_FLAG)){
            this.handleHelloRpcReply(xml);
        }
    }

    /**
     * 处理 hello 报文的返回
     */
    private void handleHelloRpcReply(String xml) throws JsonProcessingException {
        HelloRpc rpcReply = XMLMAPPER_RESOURCES.get().readValue(xml, HelloRpc.class);
        if(rpcReply == null){
            return;
        }
        if(rpcReply.getSessionId() == null){
            return;
        }

        this.lastReplyHasError = false;
        this.lastReplyHasWarning = false;
        this.lastReplyIsSuccess = true;
        this.serverCapabilities = rpcReply.getCapabilities();
        this.sessionId = rpcReply.getSessionId();
    }

    /**
     * 处理普通rpc报文的返回
     */
    private void handleCommonRpcReply(String xml) throws JsonProcessingException {
        RpcReply<Void> rpcReply = XMLMAPPER_RESOURCES.get().readValue(xml, new TypeReference<RpcReply<Void>>() {});
        if(rpcReply == null){
            return;
        }

        //执行成功标识
        boolean ok = (rpcReply.getOk() != null);
        //标识错误的严重级别
        String errorSeverity = (rpcReply.getError() == null ? null : rpcReply.getError().getErrorSeverity());

        if(errorSeverity != null){
            switch (errorSeverity) {
                case "error": this.lastReplyHasError = true; break;
                case "warning": this.lastReplyHasWarning = true; break;
                default:
            }
        }

        //返回执行成功标识，或者没有返回错误信息
        if(ok || (errorSeverity == null)){
            this.lastReplyIsSuccess = true;
        }
    }

    /**
     * Adds an Attribute to the set of RPC attributes used in the RPC XML envelope. Resets the rpcAttributes value
     * to null for generation on the next request.
     *
     * @param name The attribute name for the new attribute.
     * @param value The attribute value for the new attribute.
     */
    public synchronized void addRpcAttribute(String name, String value) {
        rpcAttrMap.put(name, value);
        rpcAttributes = null;
    }

    /**
     * Removes an attribute from the set of RPC attributes used in the RPC XML envelope. Resets the rpcAttributes value
     * to null for generation on the next request.
     *
     * @param name The attribute name to be removed.
     *
     * @return The value of the removed attribute.
     */
    public synchronized String removeRpcAttribute(String name) {
        rpcAttributes = null;
        return rpcAttrMap.remove(name);
    }

    /**
     * Clears all the RPC attributes from the set of RPC attributes used in the RPC XML envelope. The set will be empty
     * after this call returns. Resets the rpcAttributes value to null for generation on the next request.
     */
    public synchronized void removeAllRpcAttributes() {
        rpcAttrMap.clear();
        rpcAttributes = null;
    }
}
