package cn.zifangsky.netconf.adapter.huawei.secPolicy;

import cn.zifangsky.netconf.adapter.huawei.HuaWeiConstants;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.SecPolicy;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.SecPolicyData;
import cn.zifangsky.netconf.core.NetconfConstants;
import cn.zifangsky.netconf.core.RpcManager;
import cn.zifangsky.netconf.core.enums.ErrorOptionEnums;
import cn.zifangsky.netconf.core.enums.OperationEnums;
import cn.zifangsky.netconf.core.enums.TargetEnums;
import cn.zifangsky.netconf.core.model.RpcReply;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static cn.zifangsky.netconf.core.AbstractExecutingRpcManager.XMLMAPPER_RESOURCES;

/**
 * 安全策略相关方法
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 */
public class SecPolicyResolverImpl implements SecPolicyResolver {

    /**
     * NETCONF基本操作的入口
     */
    private RpcManager rpcManager;

    /**
     * <sec-policy>、<traffic-policy>等层级的属性
     */
    private String operationAttributes;


    public SecPolicyResolverImpl(RpcManager rpcManager) {
        this(rpcManager, null);
    }

    public SecPolicyResolverImpl(RpcManager rpcManager, Map<String, String> operationAttrMap) {
        if(rpcManager == null){
            throw new IllegalArgumentException("Parameter 'rpcManager' cannot be empty.");
        }
        this.rpcManager = rpcManager;

        //初始化operationAttributes
        if(operationAttrMap == null){
            operationAttrMap = this.applyDefaultOperationAttrMapIfNecessary();
        }
        this.initOperationAttributes(operationAttrMap);
    }

    @Override
    public boolean createSecPolicy(SecPolicy newSecPolicy) throws IOException {
        if(newSecPolicy == null){
            throw new IllegalArgumentException("Parameter 'newSecPolicy' cannot be empty.");
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(newSecPolicy);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecPolicy(xmlStr);

        //2. 在 rule 层级添加「create」标识
        xmlStr = xmlStr.replaceAll("<rule>", "<rule" + this.addOperationNamespace(OperationEnums.CREATE) + ">");

        //3. 创建安全策略
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否创建成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean editSecPolicy(SecPolicy secPolicy) throws IOException {
        if(secPolicy == null){
            throw new IllegalArgumentException("Parameter 'secPolicy' cannot be empty.");
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(secPolicy);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecPolicy(xmlStr);

        //2. 在 rule 层级添加「replace」标识
        xmlStr = xmlStr.replaceAll("<rule>", "<rule" + this.addOperationNamespace(OperationEnums.REPLACE) + ">");

        //3. 修改安全策略
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否修改成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public SecPolicy getSecPolicy(SecPolicy filter) throws IOException {
        if(filter == null){
            filter = new SecPolicy();
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(filter);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecPolicy(xmlStr);

        //2. 查看安全策略
        String rpcReply = this.rpcManager.getConfig(TargetEnums.RUNNING, xmlStr);

        //3. 返回安全策略相关数据
       return this.parseTheReturnResult(rpcReply);
    }

    @Override
    public boolean deleteSecPolicy(SecPolicy secPolicy) throws IOException {
        if(secPolicy == null){
            throw new IllegalArgumentException("Parameter 'secPolicy' cannot be empty.");
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(secPolicy);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecPolicy(xmlStr);

        //2. 在 rule 层级添加「delete」标识
        xmlStr = xmlStr.replaceAll("<rule>", "<rule" + this.addOperationNamespace(OperationEnums.DELETE) + ">");

        //3. 删除安全策略
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否删除成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public SecPolicy getSecPolicyHitTimes(SecPolicy filter) throws IOException {
        if(filter == null){
            filter = new SecPolicy();
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(filter);
        //1. 将 <sec-policy> 替换成 <sec-policy-state>，并添加指定的 namespace
        xmlStr = this.addNamespaceForSecPolicyState(xmlStr);

        //2. 查看安全策略的命中次数
        String rpcReply = this.rpcManager.get(xmlStr);

        //3. 返回安全策略状态相关数据
        rpcReply = rpcReply.replace("<sec-policy-state", "<sec-policy").replace("</sec-policy-state>", "</sec-policy>");
        return this.parseTheReturnResult(rpcReply);
    }





    /**
     * 解析返回结果
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param rpcReply NETCONF Server的返回结果
     * @return cn.zifangsky.netconf.adapter.huawei.secPolicy.model.SecPolicy
     */
    protected SecPolicy parseTheReturnResult(String rpcReply) throws JsonProcessingException {
        RpcReply<SecPolicyData> rpcReplyObj = XMLMAPPER_RESOURCES.get().readValue(rpcReply, new TypeReference<RpcReply<SecPolicyData>>() {
        });
        if(rpcReplyObj == null){
            return null;
        }

        SecPolicyData data = rpcReplyObj.getData();
        if(data == null){
            return null;
        }

        return data.getSecPolicy();
    }

    /**
     * 如果必要生成默认的operationAttrMap
     * @author zifangsky
     * @date 2021/2/9
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    protected Map<String, String> applyDefaultOperationAttrMapIfNecessary() {
        Map<String, String> defaultOperationAttrMap = new LinkedHashMap<>(4);

        defaultOperationAttrMap.put("xmlns", HuaWeiConstants.URN_HUAWEI_SECURITY_POLICY);
        defaultOperationAttrMap.put("xmlns:nc", NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0);
        defaultOperationAttrMap.put("xmlns:yang", NetconfConstants.URN_IETF_XML_NS_YANG_1);
        return defaultOperationAttrMap;
    }

    /**
     * 在 rule 层级添加「create/replace/delete」标识
     * @param operation 动作
     * @return java.lang.String
     */
    protected String addOperationNamespace(OperationEnums operation){
        if(operation == null){
            throw new IllegalArgumentException("Parameter 'operation' cannot be empty.");
        }

        return String.format(" %1s=\"%2s\"", "nc:operation", operation.getCode());
    }

    /**
     * 初始化<sec-policy>、<traffic-policy>等层级的属性
     */
    private void initOperationAttributes(Map<String, String> operationAttrMap) {
        StringBuilder attributes = new StringBuilder();
        for (Map.Entry<String, String> attribute : operationAttrMap.entrySet()) {
            attributes.append(String.format(" %1s=\"%2s\"", attribute.getKey(), attribute.getValue()));
        }

        this.operationAttributes = attributes.toString();
    }

    /**
     * 替 <sec-policy> 添加 Namespace
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param secPolicyTree &#60;sec-policy&#62;转换成的xml字符串
     * @return java.lang.String
     */
    private String addNamespaceForSecPolicy(String secPolicyTree){
        return secPolicyTree.replace("<sec-policy/>", "<sec-policy></sec-policy>")
                .replace("<sec-policy>", "<sec-policy" + this.operationAttributes + ">");
    }

    /**
     * 将 <sec-policy> 替换成 <sec-policy-state>，并添加指定的 namespace
     */
    private String addNamespaceForSecPolicyState(String secPolicyTree){
        //1. 将 <sec-policy> 替换成 <sec-policy-state>
        secPolicyTree = secPolicyTree.replace("<sec-policy/>", "<sec-policy></sec-policy>")
                .replace("<sec-policy", "<sec-policy-state").replace("</sec-policy>", "</sec-policy-state>");

        //2. 添加指定的 namespace
        return secPolicyTree.replace("<sec-policy-state>", "<sec-policy-state" + this.operationAttributes + ">");
    }
}
