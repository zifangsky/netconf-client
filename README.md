# netconf-client

This is a NETCONF Java library, and then we plan to support devices from multiple companies using the NETCONF protocol, such as: Huawei firewall.

## SUPPORT

This repository is forked from [Juniper/netconf-java](https://github.com/Juniper/netconf-java), after a series of refactoring and adding new features, it's now maintained by [zifangsky](https://www.zifangsky.cn/).

To report bug-fixes, issues, suggestions, please visit [Issues](https://github.com/zifangsky/netconf-client/issues).

## REQUIREMENTS

- [OpenJDK 8](http://openjdk.java.net/projects/jdk8/) or Java 8
- [Maven](https://maven.apache.org/download.cgi) if you want to build using `mvn` [Supported from v2.1.1].
- [lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok) needs to be provided by the runtime (`mvn dependency scope` is set as `provided`)

## Usage

#### i). Instructions to usr jar

```xml
<dependency>
    <groupId>cn.zifangsky</groupId>
    <artifactId>netconf-java</artifactId>
    <version>0.9.0</version>
</dependency>
```

#### ii). Instructions to build using `mvn`

- Download Source Code for the required release
- Compile the code and install the jar to maven **local repository**, using `mvn install`
- Use the netconf-java dependency file from maven local repository



The basic RPC operations related to NETCONF protocol, you can refer to the `cn.zifangsky.netconf.core.RpcManager` interface, such as:

```java
/**
 * 返回与 Netconf 是否还保持连接状态
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @return return true if connected
 */
boolean isConnected();

/**
 * 从Netconf session获取 sessionId
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @return Session ID
 */
String getSessionId();

/**
 * 发起一个RPC请求
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param rpcContent RPC content
 * @return RPC reply sent by Netconf server
 * @throws IOException If there are errors communicating with the netconf server.
 */
String executeRpc(String rpcContent) throws IOException;

/**
 * 发起一个RPC请求
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
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
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param command the cli command to be executed.
 * @return result of the command, as a String.
 * @throws IOException If there are errors communicating with the netconf server.
 */
String runCliCommand(String command) throws IOException;

/**
 * 执行一个&#60;cli&#62;命令
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param command the cli command to be executed.
 * @return result of the command, as a BufferedReader. This is
 * useful if we want continuous stream of output, rather than wait
 * for whole output till command execution completes.
 * @throws IOException If there are errors communicating with the netconf server.
 */
BufferedReader runCliCommandRunning(String command) throws IOException;

/**
 * 执行&#60;get&#62;命令，示例：
 * <get>
 *     <filter type="subtree">
 *         <top xmlns="http://example.com/schema/1.2/stats">
 *             <interfaces>
 *                 <interface>
 *                     <ifName>eth0</ifName>
 *                 </interface>
 *             </interfaces>
 *         </top>
 *     </filter>
 * </get>
 *
 * <p>Retrieve running configuration and device state information.</p>
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 * @param filterTree filter正文部分
 * @return result of the command, as a String.
 * @throws IOException If there are errors communicating with the netconf server.
 */
String get(String filterTree) throws IOException;

/**
 * 执行&#60;get-config&#62;命令，示例：
 * <get-config>
 *     <source>
 *         <running/>
 *     </source>
 *     <filter type="subtree">
 *         <top xmlns="http://example.com/schema/1.2/config">
 *             <users/>
 *         </top>
 *     </filter>
 * </get-config>
 *
 * <p>Retrieve all or part of a specified configuration datastore.</p>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param source source节点
 * @param filterTree filter正文部分
 * @return result of the command, as a String.
 * @throws IOException If there are errors communicating with the netconf server.
 */
String getConfig(TargetEnums source, String filterTree) throws IOException;

/**
 * 获取 candidate 配置
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param filterTree filter正文部分
 * @return configuration data as a string.
 * @throws IOException If there are errors communicating with the netconf server.
 */
String getCandidateConfig(String filterTree) throws IOException;

/**
 * 获取 running 配置
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param filterTree filter正文部分
 * @return configuration data as a string.
 * @throws IOException If there are errors communicating with the netconf server.
 */
String getRunningConfig(String filterTree) throws IOException;

/**
 * 执行&#60;edit-config&#62;命令，默认source为running
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param configTree config正文部分
 * @return result of the command, as a String.
 * @throws IOException If there are errors communicating with the netconf server.
 */
String editRunningConfig(String configTree) throws IOException;

/**
 * 执行&#60;edit-config&#62;命令，示例：
 * <edit-config>
 *     <target>
 *         <running/>
 *     </target>
 *     <config xmlns:xc="urn:ietf:params:xml:ns:netconf:base:1.0">
 *         <top xmlns="http://example.com/schema/1.2/config">
 *             <interface xc:operation="replace">
 *                 <name>Ethernet0/0</name>
 *                 <mtu>1500</mtu>
 *                 <address>
 *                     <name>192.0.2.4</name>
 *                     <prefix-length>24</prefix-length>
 *                 </address>
 *             </interface>
 *         </top>
 *     </config>
 * </edit-config>
 *
 * <p>The <edit-config> operation loads all or part of a specified
 *       configuration to the specified target configuration datastore.
 *       This operation allows the new configuration to be expressed in
 *       several ways, such as using a local file, a remote file, or
 *       inline.  If the target configuration datastore does not exist, it
 *       will be created.
 * </p>
 * <p>If a NETCONF peer supports the :url capability (https://tools.ietf.org/html/rfc6241#section-8.8), the
 *       <url> element can appear instead of the <config> parameter.
 * </p>
 * <p> The device analyzes the source and target configurations and
 *       performs the requested changes.  The target configuration is not
 *       necessarily replaced, as with the <copy-config> message.  Instead,
 *       the target configuration is changed in accordance with the
 *       source's data and requested operations.
 *  </p>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param target target节点
 * @param configTree config正文部分
 * @return result of the command, as a String.
 * @throws IOException If there are errors communicating with the netconf server.
 */
String editConfig(TargetEnums target, String configTree) throws IOException;

/**
 * 执行&#60;edit-config&#62;命令，示例：
 * <edit-config>
 *     <target>
 *         <running/>
 *     </target>
 *     <default-operation>none</default-operation>
 *     <error-option>rollback-on-error</error-option>
 *     <config xmlns:xc="urn:ietf:params:xml:ns:netconf:base:1.0">
 *         <top xmlns="http://example.com/schema/1.2/config">
 *             <interface xc:operation="delete">
 *                 <name>Ethernet0/0</name>
 *             </interface>
 *         </top>
 *     </config>
 * </edit-config>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param target target节点
 * @param defaultOperation default-operation节点
 * @param testOption test-option节点
 * @param errorOption error-option节点
 * @param configTree config正文部分
 * @return result of the command, as a String.
 * @throws IOException If there are errors communicating with the netconf server.
 */
String editConfig(TargetEnums target, DefaultOperationEnums defaultOperation,
                  TestOptionEnums testOption, ErrorOptionEnums errorOption, String configTree) throws IOException;

/**
 * 执行&#60;copy-config&#62;命令，示例：
 * <copy-config>
 *     <target>
 *         <startup/>
 *     </target>
 *     <source>
 *         <running/>
 *     </source>
 * </copy-config>
 *
 * <p>Create or replace an entire configuration datastore
 *       with the contents of another complete configuration datastore.  If
 *       the target datastore exists, it is overwritten.  Otherwise, a new
 *       one is created, if allowed.
 * </p>
 * <p>If a NETCONF peer supports the :url capability (https://tools.ietf.org/html/rfc6241#section-8.8), the
 *       <url> element can appear as the <source> or <target> parameter.
 * </p>
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 * @param source source节点
 * @param target target节点
 * @return true if successful.
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean copyConfig(TargetEnums target, TargetEnums source) throws IOException;

/**
 * 执行&#60;copy-config&#62;命令，示例：
 * <copy-config>
 *     <target>
 *         <running/>
 *     </target>
 *     <source>
 *         <url>https://user:password@example.com/cfg/new.txt</url>
 *     </source>
 * </copy-config>
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 * @param sourceUrl sourceUrl
 * @param target target节点
 * @return true if successful.
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean copyConfig(TargetEnums target, String sourceUrl) throws IOException;

/**
 * 执行&#60;delete-config&#62;命令，示例：
 * <delete-config>
 *     <target>
 *         <startup/>
 *     </target>
 * </delete-config>
 *
 * <p>Delete a configuration datastore. The <running> configuration datastore cannot be deleted.
 * </p>
 * <p>If a NETCONF peer supports the :url capability (https://tools.ietf.org/html/rfc6241#section-8.8), the
 *       <url> element can appear as the <source> or <target> parameter.
 * </p>
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 * @param target target节点
 * @return true if successful.
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean deleteConfig(TargetEnums target) throws IOException;

/**
 * 加锁
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @return true if successful.
 * @throws IOException If there are issues communicating with the netconf server.
 */
boolean lock() throws IOException;

/**
 * 加锁，示例：
 * <lock>
 *     <target>
 *         <running/>
 *     </target>
 * </lock>
 *
 * <p>The <lock> operation allows the client to lock the entire configuration datastore system of a device.
 *       Such locks are intended to be short-lived and allow a client to make a change without fear of
 *       interaction with other NETCONF clients, nonNETCONF clients (e.g., SNMP and command line interface (CLI)
 *       scripts), and human users.
 * </p>
 * <p>An attempt to lock the configuration datastore MUST fail if an existing session or other entity holds
 *       a lock on any portion of the lock target.
 * </p>
 * <p>When the lock is acquired, the server MUST prevent any changes to the locked resource other than those
 *       requested by this session. SNMP and CLI requests to modify the resource MUST fail with an
 *       appropriate error.
 * </p>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param source source节点
 * @return true if successful.
 * @throws IOException If there are issues communicating with the netconf server.
 */
boolean lock(TargetEnums source) throws IOException;

/**
 * 解锁
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @return true if successful.
 * @throws IOException If there are issues communicating with the netconf server.
 */
boolean unlock() throws IOException;

/**
 * 解锁，示例：
 * <lock>
 *     <target>
 *         <running/>
 *     </target>
 * </lock>
 *
 * <p>The <unlock> operation is used to release a configuration lock, previously
 *       obtained with the <lock> operation.
 * </p>
 * <p>An <unlock> operation will not succeed if either of the following
 *       conditions is true:
 *   <ul>
 *       <li>The specified lock is not currently active.</li>
 *       <li>The session issuing the <unlock> operation is not the same session that obtained the lock.</li>
 *   </ul>
 * </p>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param source source节点
 * @return true if successful.
 * @throws IOException If there are issues communicating with the netconf server.
 */
boolean unlock(TargetEnums source) throws IOException;

/**
 * 提交，示例：
 * <commit/>
 *
 * <p> When the candidate configuration's content is complete, the
 *          configuration data can be committed, publishing the data set to
 *          the rest of the device and requesting the device to conform to
 *          the behavior described in the new configuration.
 * </p>
 * <p>To commit the candidate configuration as the device's new current configuration, use the <commit> operation.</p>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @return true if successful.
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean commit() throws IOException;

/**
 * 确定并提交，示例：
 * <commit>
 *     <confirmed/>
 *     <confirm-timeout>120</confirm-timeout>
 * </commit>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @return true if successful.
 * @param confirmTimeout Timeout period for confirmed commit, in seconds. If unspecified, the confirm timeout defaults to 600 seconds.
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean commitConfirm(int confirmTimeout) throws IOException;

/**
 * 验证，示例：
 * <validate>
 *     <source>
 *         <candidate/>
 *     </source>
 * </validate>
 *
 * <p>This protocol operation validates the contents of the specified configuration.</p>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param source Name of the configuration datastore to validate, such as <candidate>, or the <config> element containing the complete
 *               configuration to validate.
 * @return true if the device was able to satisfy the request.
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean validate(TargetEnums source) throws IOException;

/**
 * 优雅地关闭 NETCONF 会话，示例：
 * <close-session/>
 *
 * <p>When a NETCONF server receives a <close-session> request, it will
 *       gracefully close the session.  The server will release any locks
 *       and resources associated with the session and gracefully close any
 *       associated connections.  Any NETCONF requests received after a
 *       <close-session> request will be ignored.
 * </p>
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @return true if successful.
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean closeSession() throws IOException;

/**
 * 强制关闭 NETCONF 会话，示例：
 * <kill-session>
 *     <session-id>4</session-id>
 * </kill-session
 *
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
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 * @param sessionId Session identifier of the NETCONF session to be terminated.
 *                  If this value is equal to the current session ID, an "invalid-value" error is returned.
 * @return true if successful.
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean killSession(int sessionId) throws IOException;

/**
 * 检查RPC请求的返回报文是否执行成功
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 * @param rpcReply RPC请求的返回报文
 * @return boolean
 * @throws IOException If there are errors communicating with the netconf server.
 */
boolean checkIsSuccess(String rpcReply) throws IOException;
```



For how to interact with the device through the NETCONF protocol, you can refer to the following examples and other test classes in the `src/test/java/cn/zifangsky/netconf` package.

```java
private static final String TEST_HOSTNAME = "127.0.0.1";
private static final String TEST_USERNAME = "netconf";
private static final String TEST_PASSWORD = "admin123456";
private static final int DEFAULT_NETCONF_PORT = 830;

private static DefaultRpcManager rpcManager;

@BeforeAll
public static void init() throws NetconfException {
    Device device = Device.builder()
            .hostName(TEST_HOSTNAME)
            .userName(TEST_USERNAME)
            .password(TEST_PASSWORD)
            .port(DEFAULT_NETCONF_PORT)
            .strictHostKeyChecking(false)
            .build();

    rpcManager = new DefaultRpcManager(device);
}

/**
 * 测试连接情况
 */
@Test
@DisplayName("测试连接情况")
public void GIVEN_requiredParameters_THEN_createDevice() throws NetconfException {
    System.out.println(rpcManager.getSessionId() + ": " + rpcManager.isConnected());
}

/**
 * 查看安全策略配置
 */
@Test
@DisplayName("查看安全策略配置")
public void getConfigSecPolicy() throws Exception {
    String xml = rpcManager.executeRpc("<get-config><source><running/></source><filter type=\"subtree\"><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"></sec-policy></filter></get-config>");
    System.out.println(xml);
}
```



For more information about the NETCONF protocol, please refer to: [https://tools.ietf.org/html/rfc6241](https://tools.ietf.org/html/rfc6241)

AUTHOR
------

[zifangsky](https://www.zifangsky.cn/), [Ankit Jain](http://www.linkedin.com/in/ankitj093), Juniper Networks
[Peter J Hill](https://github.com/peterjhill), Oracle