package cn.zifangsky.netconf.core;

import cn.zifangsky.netconf.core.enums.DefaultOperationEnums;
import cn.zifangsky.netconf.core.enums.ErrorOptionEnums;
import cn.zifangsky.netconf.core.enums.TargetEnums;
import cn.zifangsky.netconf.core.enums.TestOptionEnums;
import cn.zifangsky.netconf.core.model.HelloRpc;
import cn.zifangsky.netconf.core.model.RpcError;
import cn.zifangsky.netconf.core.model.RpcReply;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static cn.zifangsky.netconf.core.NetconfSession.COMMON_RPC_REPLY_FLAG;
import static cn.zifangsky.netconf.core.NetconfSession.HELLO_RPC_REPLY_FLAG;

/**
 * NETCONF相关的基本RPC操作
 * <p>更多参考：https://tools.ietf.org/html/rfc6241</p>
 *
 * @author zifangsky
 * @date 21/2/7
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractExecutingRpcManager extends AbstractRoutingRpcManager implements AutoCloseable {
    public static final ThreadLocal<XmlMapper> XMLMAPPER_RESOURCES = ThreadLocal.withInitial(() -> {
        XmlMapper xmlMapper = new XmlMapper();
        //允许忽略不存在的字段
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //允许特殊字符
        xmlMapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        //属性值为Null时不参与序列化
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return xmlMapper;
    });

    /**
     * 通过 NETCONF 执行命令
     */
    public static final String RUN_CLI_COMMAND_TEMPLATE = "<command format=\"text\">" +
                                                              "{0}" +
                                                          "</command>";
    /**
     * &#60;get&#62;报文
     */
    public static final String GET_TEMPLATE =   "<get>" +
                                                    "<filter type=\"subtree\">" +
                                                        "{0}" +
                                                    "</filter>" +
                                                "</get>";
    /**
     * &#60;get-config&#62;报文
     */
    public static final String GET_CONFIG_TEMPLATE = "<get-config>" +
                                                          "<source>" +
                                                              "<{0}/>" +
                                                          "</source>" +
                                                          "{1}" +
                                                      "</get-config>";
    /**
     * &#60;get-config&#62;过滤条件
     */
    public static final String GET_CONFIG_FILTER = "<filter type=\"subtree\">{0}</filter>";

    /**
     * &#60;edit-config&#62;报文（&#60;default-operation&#62;、&#60;test-option&#62;、&#60;error-option&#62;）
     */
    public static final String EDIT_CONFIG_TEMPLATE = "<edit-config>" +
                                                            "<target>" +
                                                                "<{0}/>" +
                                                            "</target>" +
                                                            "{1}" +
                                                            "{2}" +
                                                            "{3}" +
                                                            "<config>" +
                                                                "{4}" +
                                                            "</config>" +
                                                        "</edit-config>";
    /**
     * &#60;edit-config&#62;的&#60;default-operation&#62;节点
     */
    public static final String EDIT_CONFIG_DEFAULT_OPERATION = "<default-operation>{0}</default-operation>";
    /**
     * &#60;edit-config&#62;的&#60;test-option&#62;节点
     */
    public static final String EDIT_CONFIG_TEST_OPTION = "<test-option>{0}</test-option>";
    /**
     * &#60;edit-config&#62;的&#60;error-option&#62;节点
     */
    public static final String EDIT_CONFIG_ERROR_OPTION = "<error-option>{0}</error-option>";
    /**
     * &#60;copy-config&#62;报文
     */
    public static final String COPY_CONFIG_TEMPLATE = "<copy-config>" +
                                                            "<target>" +
                                                                "<{0}/>" +
                                                            "</target>" +
                                                            "<source>" +
                                                                "<{1}/>" +
                                                            "</source>" +
                                                        "</copy-config>";
    /**
     * &#60;copy-config&#62;报文
     */
    public static final String COPY_CONFIG_BY_URL_TEMPLATE = "<copy-config>" +
                                                                  "<target>" +
                                                                      "<{0}/>" +
                                                                  "</target>" +
                                                                  "<source>" +
                                                                      "<url>{1}</url>" +
                                                                  "</source>" +
                                                              "</copy-config>";
    /**
     * &#60;delete-config&#62;报文
     */
    public static final String DELETE_CONFIG_TEMPLATE = "<delete-config>" +
                                                            "<target>" +
                                                                "<{0}/>" +
                                                            "</target>" +
                                                        "</delete-config>";

    /**
     * &#60;lock&#62;报文
     */
    public static final String LOCK_TEMPLATE = "<lock>" +
                                                    "<target>" +
                                                        "<{0}/>" +
                                                    "</target>" +
                                                "</lock>";
    /**
     * &#60;unlock&#62;报文
     */
    public static final String UNLOCK_TEMPLATE = "<unlock>" +
                                                      "<target>" +
                                                          "<{0}/>" +
                                                      "</target>" +
                                                  "</unlock>";
    /**
     * &#60;commit&#62;报文
     */
    public static final String COMMIT_TEMPLATE = "<commit/>";
    /**
     * &#60;commit&#62;报文（&#60;confirmed&#62;）
     */
    public static final String COMMIT_CONFIRM_TEMPLATE = "<commit>" +
                                                              "<confirmed/>" +
                                                              "<confirm-timeout>{0}</confirm-timeout>" +
                                                          "</commit>";
    /**
     * &#60;validate&#62;报文（&#60;confirmed&#62;）
     */
    public static final String VALIDATE_TEMPLATE = "<validate>" +
                                                        "<source>" +
                                                            "<{0}/>" +
                                                        "</source>" +
                                                    "</validate>";
    /**
     * &#60;close-session&#62;报文
     */
    public static final String CLOSE_SESSION_TEMPLATE = "<close-session/>";
    /**
     * &#60;kill-session&#62;报文
     */
    public static final String KILL_SESSION_TEMPLATE = "<kill-session>" +
                                                            "<session-id>{0}</session-id>" +
                                                        "</kill-session>";

    public AbstractExecutingRpcManager(Device defaultDevice) {
        this(new HashMap<>(), defaultDevice);
    }

    public AbstractExecutingRpcManager(Map<Object, Device> targetDevices, Device defaultDevice) {
        super(targetDevices, defaultDevice);
    }

    @Override
    public boolean isConnected() throws IOException {
        return this.determineTargetDevice().isConnected();
    }

    @Override
    public String getSessionId() throws IOException {
        return this.determineTargetDevice().getSessionId();
    }

    @Override
    public String executeRpc(String rpcContent) throws IOException {
        return this.determineTargetDevice().executeRpc(rpcContent);
    }

    @Override
    public BufferedReader executeRpcRunning(String rpcContent) throws IOException {
        return this.determineTargetDevice().executeRpcRunning(rpcContent);
    }

    @Override
    public String runCliCommand(String command) throws IOException {
        String rpcContent = MessageFormat.format(RUN_CLI_COMMAND_TEMPLATE, command);
        return this.executeRpc(rpcContent);
    }

    @Override
    public BufferedReader runCliCommandRunning(String command) throws IOException {
        String rpcContent = MessageFormat.format(RUN_CLI_COMMAND_TEMPLATE, command);
        return this.executeRpcRunning(rpcContent);
    }

    @Override
    public String get(String filterTree) throws IOException {
        if(StringUtils.isBlank(filterTree)){
            throw new IllegalArgumentException("Parameter 'filterTree' cannot be empty.");
        }

        String rpcContent = MessageFormat.format(GET_TEMPLATE, filterTree);
        return this.executeRpc(rpcContent);
    }

    @Override
    public String getConfig(TargetEnums source, String filterTree) throws IOException {
        if(source == null){
            source = TargetEnums.RUNNING;
        }

        String filter = "";
        if(StringUtils.isNoneBlank(filterTree)){
            filter = MessageFormat.format(GET_CONFIG_FILTER, filterTree);
        }

        String rpcContent = MessageFormat.format(GET_CONFIG_TEMPLATE, source.getCode(), filter);
        return this.executeRpc(rpcContent);
    }

    @Override
    public String getCandidateConfig(String filterTree) throws IOException {
        return this.getConfig(TargetEnums.CANDIDATE, filterTree);
    }

    @Override
    public String getRunningConfig(String filterTree) throws IOException {
        return this.getConfig(TargetEnums.RUNNING, filterTree);
    }

    @Override
    public String editRunningConfig(String configTree) throws IOException {
        return this.editConfig(TargetEnums.RUNNING, configTree);
    }

    @Override
    public String editConfig(TargetEnums target, String configTree) throws IOException {
        return this.editConfig(target, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, configTree);
    }

    @Override
    public String editConfig(TargetEnums target, DefaultOperationEnums defaultOperation, TestOptionEnums testOption, ErrorOptionEnums errorOption, String configTree) throws IOException {
        if(StringUtils.isBlank(configTree)){
            throw new IllegalArgumentException("Parameter 'configTree' cannot be empty.");
        }

        if(target == null){
            target = TargetEnums.RUNNING;
        }
        String defaultOperationStr = "";
        if(defaultOperation != null){
            defaultOperationStr = MessageFormat.format(EDIT_CONFIG_DEFAULT_OPERATION, defaultOperation.getCode());
        }
        String testOptionStr = "";
        if(testOption != null){
            testOptionStr = MessageFormat.format(EDIT_CONFIG_TEST_OPTION, testOption.getCode());
        }
        String errorOptionStr = "";
        if(errorOption != null){
            testOptionStr = MessageFormat.format(EDIT_CONFIG_ERROR_OPTION, errorOption.getCode());
        }

        String rpcContent = MessageFormat.format(EDIT_CONFIG_TEMPLATE, target.getCode(), defaultOperationStr, testOptionStr, errorOptionStr, configTree);
        return this.executeRpc(rpcContent);
    }

    @Override
    public boolean copyConfig(TargetEnums target, TargetEnums source) throws IOException {
        if(target == null){
            target = TargetEnums.RUNNING;
        }
        if(source == null){
            throw new IllegalArgumentException("Parameter 'source' cannot be empty.");
        }

        String rpcContent = MessageFormat.format(COPY_CONFIG_TEMPLATE, target.getCode(), source.getCode());
        String rpcReply = this.executeRpc(rpcContent);

        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean copyConfig(TargetEnums target, String sourceUrl) throws IOException {
        if(target == null){
            target = TargetEnums.RUNNING;
        }
        if(StringUtils.isBlank(sourceUrl)){
            throw new IllegalArgumentException("Parameter 'sourceUrl' cannot be empty.");
        }

        String rpcContent = MessageFormat.format(COPY_CONFIG_BY_URL_TEMPLATE, target.getCode(), sourceUrl);
        String rpcReply = this.executeRpc(rpcContent);

        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean deleteConfig(TargetEnums target) throws IOException {
        if(target == null){
            throw new IllegalArgumentException("Parameter 'target' cannot be empty.");
        }

        String rpcContent = MessageFormat.format(DELETE_CONFIG_TEMPLATE, target.getCode());
        String rpcReply = this.executeRpc(rpcContent);

        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean lock() throws IOException {
        return this.lock(TargetEnums.RUNNING);
    }

    @Override
    public boolean lock(TargetEnums source) throws IOException {
        if(source == null){
            source = TargetEnums.RUNNING;
        }
        String rpcContent = MessageFormat.format(LOCK_TEMPLATE, source.getCode());
        String rpcReply = this.executeRpc(rpcContent);
        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean unlock() throws IOException {
        return this.unlock(TargetEnums.RUNNING);
    }

    @Override
    public boolean unlock(TargetEnums source) throws IOException {
        if(source == null){
            source = TargetEnums.RUNNING;
        }
        String rpcContent = MessageFormat.format(UNLOCK_TEMPLATE, source.getCode());
        String rpcReply = this.executeRpc(rpcContent);
        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean commit() throws IOException {
        String rpcReply = this.executeRpc(COMMIT_TEMPLATE);

        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean commitConfirm(int confirmTimeout) throws IOException {
        String rpcContent = MessageFormat.format(COMMIT_CONFIRM_TEMPLATE, confirmTimeout);
        String rpcReply = this.executeRpc(rpcContent);

        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean validate(TargetEnums source) throws IOException {
        String rpcContent = MessageFormat.format(VALIDATE_TEMPLATE, source.getCode());
        String rpcReply = this.executeRpc(rpcContent);

        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean closeSession() throws IOException {
        String rpcReply = this.executeRpc(CLOSE_SESSION_TEMPLATE);

        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean killSession(int sessionId) throws IOException {
        String rpcContent = MessageFormat.format(KILL_SESSION_TEMPLATE, sessionId);
        String rpcReply = this.executeRpc(rpcContent);

        //返回执行结果
        return this.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean checkIsSuccess(String rpcReply) throws IOException {
        if(StringUtils.isBlank(rpcReply)){
            return false;
        }

        if(rpcReply.contains(COMMON_RPC_REPLY_FLAG)){
            return this.checkCommonRpcReply(rpcReply);
        }
        else if(rpcReply.contains(HELLO_RPC_REPLY_FLAG)){
            return this.checkHelloRpcReply(rpcReply);
        }

        return false;
    }

    @Override
    public RpcError getLastRpcErrorDetails() throws IOException {
        Device device = this.determineTargetDevice();

        //如果上次执行没有失败，则直接返回
        if(!device.lastReplyHasError()){
            return null;
        }

        //反序列化上次执行结果
        RpcReply<Void> rpcReply = this.getCommonRpcReply(device.getLastRpcReply());
        if(rpcReply == null){
            return null;
        }
        //返回错误详情
        return rpcReply.getError();
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
        try {
            Device device = this.determineTargetDevice();
            device.doClose();
        }catch (Exception e){
            //ignore
        }
    }

    /**
     * 检查普通rpc报文的返回
     */
    private boolean checkCommonRpcReply(String xml) throws JsonProcessingException {
        RpcReply<Void> rpcReply = this.getCommonRpcReply(xml);
        if(rpcReply == null){
            return false;
        }

        //执行成功标识
        boolean ok = (rpcReply.getOk() != null);
        //标识错误的严重级别
        String errorSeverity = (rpcReply.getError() == null ? null : rpcReply.getError().getErrorSeverity());

        //返回执行成功标识，或者没有返回错误信息
        return (ok || (errorSeverity == null));
    }

    /**
     * 检查 hello 报文的返回
     */
    private boolean checkHelloRpcReply(String xml) throws JsonProcessingException {
        HelloRpc rpcReply = XMLMAPPER_RESOURCES.get().readValue(xml, HelloRpc.class);
        if(rpcReply == null){
            return false;
        }

        return rpcReply.getSessionId() != null;
    }

    /**
     * 反序列化普通rpc报文的返回
     */
    private RpcReply<Void> getCommonRpcReply(String xml) throws JsonProcessingException {
        return XMLMAPPER_RESOURCES.get().readValue(xml, new TypeReference<RpcReply<Void>>() {});
    }
}
