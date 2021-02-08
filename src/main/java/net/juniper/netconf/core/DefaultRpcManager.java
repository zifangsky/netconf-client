package net.juniper.netconf.core;

import net.juniper.netconf.core.enums.DefaultOperationEnums;
import net.juniper.netconf.core.enums.ErrorOptionEnums;
import net.juniper.netconf.core.enums.TargetEnums;
import net.juniper.netconf.core.enums.TestOptionEnums;
import net.juniper.netconf.core.exception.NetconfException;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * NETCONF相关的基本RPC操作
 * <p>更多参考：https://tools.ietf.org/html/rfc6241</p>
 *
 * @author zifangsky
 * @date 21/2/7
 * @since 1.0.0
 */
public class DefaultRpcManager implements RpcManager {
    /**
     * 通过 NETCONF 执行命令
     */
    public static String runCliCommandTemplate = "<command format=\"text\">" +
                                                        "{0}" +
                                                    "</command>";
    /**
     * &#60;get-config&#62;报文
     */
    public static String getConfigTemplate = "<get-config>" +
                                                    "<source>" +
                                                        "<{0}/>" +
                                                    "</source>" +
                                                    "{1}" +
                                                "</get-config>";
    /**
     * &#60;get-config&#62;过滤条件
     */
    public static String getConfigFilter = "<filter type=\"subtree\">{0}</filter>";

    /**
     * &#60;edit-config&#62;报文（&#60;default-operation&#62;、&#60;test-option&#62;、&#60;error-option&#62;）
     */
    public static String editConfigTemplate = "<edit-config>" +
                                                    "<target>" +
                                                        "<{0}/>" +
                                                    "</target>" +
                                                    "{1}" +
                                                    "{2}" +
                                                    "{3}" +
                                                    "<config>" +
                                                        "<{4}/>" +
                                                    "</config>" +
                                                "</edit-config>";
    /**
     * &#60;edit-config&#62;的&#60;default-operation&#62;节点
     */
    public static String editConfigDefaultOperation = "<default-operation>{0}</default-operation>";
    /**
     * &#60;edit-config&#62;的&#60;test-option&#62;节点
     */
    public static String editConfigTestOption = "<test-option>{0}</test-option>";
    /**
     * &#60;edit-config&#62;的&#60;error-option&#62;节点
     */
    public static String editConfigErrorOption = "<error-option>{0}</error-option>";

    /**
     * &#60;lock&#62;报文
     */
    public static String lockTemplate = "<lock>" +
                                            "<target>" +
                                                "<{0}/>" +
                                            "</target>" +
                                        "</lock>";
    /**
     * &#60;unlock&#62;报文
     */
    public static String unlockTemplate = "<unlock>" +
                                                "<target>" +
                                                    "<{0}/>" +
                                                "</target>" +
                                            "</unlock>";
    /**
     * &#60;commit&#62;报文
     */
    public static String commitTemplate = "<commit/>";
    /**
     * &#60;commit&#62;报文（&#60;confirmed&#62;）
     */
    public static String commitConfirmTemplate = "<commit>" +
                                                    "<confirmed/>" +
                                                    "<confirm-timeout>{0}</confirm-timeout>" +
                                                "</commit>";
    /**
     * &#60;validate&#62;报文（&#60;confirmed&#62;）
     */
    public static String validateTemplate = "<validate>" +
                                                "<source>" +
                                                    "<{0}/>" +
                                                "</source>" +
                                            "</validate>";
    /**
     * &#60;close-session&#62;报文
     */
    public static String closeSessionTemplate = "<close-session/>";
    /**
     * &#60;kill-session&#62;报文
     */
    public static String killSessionTemplate = "<kill-session>" +
                                                    "<session-id>{0}</session-id>" +
                                                "</kill-session>";

    /*  params  */
    /**
     * 一个与设备的连接实例
     */
    private Device device;

    public DefaultRpcManager(Device device) throws NetconfException {
        if(device == null){
            throw new IllegalArgumentException("Parameter cannot be empty.");
        }

        if(!device.isConnected()){
            device.connect();
        }

        this.device = device;
    }

    @Override
    public boolean isConnected() {
        return this.device.isConnected();
    }

    @Override
    public String getSessionId() {
        return this.device.getSessionId();
    }

    @Override
    public String executeRpc(String rpcContent) throws IOException {
        return this.device.executeRpc(rpcContent);
    }

    @Override
    public BufferedReader executeRpcRunning(String rpcContent) throws IOException {
        return this.device.executeRpcRunning(rpcContent);
    }

    @Override
    public String runCliCommand(String command) throws IOException {
        String rpcContent = MessageFormat.format(runCliCommandTemplate, command);
        return this.executeRpc(rpcContent);
    }

    @Override
    public BufferedReader runCliCommandRunning(String command) throws IOException {
        String rpcContent = MessageFormat.format(runCliCommandTemplate, command);
        return this.executeRpcRunning(rpcContent);
    }

    @Override
    public String getConfig(TargetEnums source, String filterTree) throws IOException {
        if(source == null){
            source = TargetEnums.RUNNING;
        }

        String filter = "";
        if(StringUtils.isNoneBlank(filterTree)){
            filter = MessageFormat.format(getConfigFilter, filterTree);
        }

        String rpcContent = MessageFormat.format(getConfigTemplate, source.getCode(), filter);
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
    public String editConfig(TargetEnums source, String configTree) throws IOException {
        return this.editConfig(source, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, configTree);
    }

    @Override
    public String editConfig(TargetEnums source, DefaultOperationEnums defaultOperation, TestOptionEnums testOption, ErrorOptionEnums errorOption, String configTree) throws IOException {
        if(StringUtils.isBlank(configTree)){
            throw new IllegalArgumentException("Parameter cannot be empty.");
        }

        if(source == null){
            source = TargetEnums.RUNNING;
        }
        String defaultOperationStr = "";
        if(defaultOperation != null){
            defaultOperationStr = MessageFormat.format(editConfigDefaultOperation, defaultOperation.getCode());
        }
        String testOptionStr = "";
        if(testOption != null){
            testOptionStr = MessageFormat.format(editConfigTestOption, testOption.getCode());
        }
        String errorOptionStr = "";
        if(errorOption != null){
            testOptionStr = MessageFormat.format(editConfigErrorOption, errorOption.getCode());
        }

        String rpcContent = MessageFormat.format(editConfigTemplate, source.getCode(), defaultOperationStr, testOptionStr, errorOptionStr, configTree);
        return this.executeRpc(rpcContent);
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
        String rpcContent = MessageFormat.format(lockTemplate, source.getCode());
        this.executeRpc(rpcContent);
        //返回执行结果
        return this.device.lastReplyIsSuccess();
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
        String rpcContent = MessageFormat.format(unlockTemplate, source.getCode());
        this.executeRpc(rpcContent);
        //返回执行结果
        return this.device.lastReplyIsSuccess();
    }

    @Override
    public boolean commit() throws IOException {
        this.executeRpc(commitTemplate);
        //返回执行结果
        return this.device.lastReplyIsSuccess();
    }

    @Override
    public boolean commitConfirm(int confirmTimeout) throws IOException {
        String rpcContent = MessageFormat.format(commitConfirmTemplate, confirmTimeout);
        this.executeRpc(rpcContent);
        //返回执行结果
        return this.device.lastReplyIsSuccess();
    }

    @Override
    public boolean validate() throws IOException {
        this.executeRpc(validateTemplate);
        //返回执行结果
        return this.device.lastReplyIsSuccess();
    }

    @Override
    public boolean closeSession() throws IOException {
        this.executeRpc(closeSessionTemplate);
        //返回执行结果
        return this.device.lastReplyIsSuccess();
    }

    @Override
    public boolean killSession(int sessionId) throws IOException {
        String rpcContent = MessageFormat.format(killSessionTemplate, sessionId);
        this.executeRpc(rpcContent);
        //返回执行结果
        return this.device.lastReplyIsSuccess();
    }

}
