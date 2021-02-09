package net.juniper.netconf.adapter.huawei.secPolicy;

import net.juniper.netconf.adapter.huawei.HuaWeiConstants;
import net.juniper.netconf.adapter.huawei.secPolicy.model.SecPolicy;
import net.juniper.netconf.core.NetconfConstants;
import net.juniper.netconf.core.RpcManager;
import net.juniper.netconf.core.enums.ErrorOptionEnums;
import net.juniper.netconf.core.enums.OperationEnums;
import net.juniper.netconf.core.enums.TargetEnums;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static net.juniper.netconf.core.DefaultRpcManager.XMLMAPPER_RESOURCES;

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
        xmlStr = xmlStr.replace("<sec-policy>", "<sec-policy" + this.operationAttributes + ">");

        //2. 在 rule 层级添加「create」标识
        xmlStr = xmlStr.replace("<rule>", "<rule" + this.getOperationNamespace(OperationEnums.CREATE) + ">");

        //3. 创建安全策略
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否创建成功
        return this.rpcManager.checkIsSuccess(rpcReply);
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
    protected String getOperationNamespace(OperationEnums operation){
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

}
