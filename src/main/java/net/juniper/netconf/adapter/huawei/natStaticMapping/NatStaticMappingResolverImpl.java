package net.juniper.netconf.adapter.huawei.natStaticMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import net.juniper.netconf.adapter.huawei.HuaWeiConstants;
import net.juniper.netconf.adapter.huawei.natStaticMapping.model.NatStaticMapping;
import net.juniper.netconf.adapter.huawei.natStaticMapping.model.NatStaticMappingData;
import net.juniper.netconf.core.NetconfConstants;
import net.juniper.netconf.core.RpcManager;
import net.juniper.netconf.core.enums.ErrorOptionEnums;
import net.juniper.netconf.core.enums.OperationEnums;
import net.juniper.netconf.core.enums.TargetEnums;
import net.juniper.netconf.core.model.RpcReply;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static net.juniper.netconf.core.DefaultRpcManager.XMLMAPPER_RESOURCES;

/**
 * 静态映射相关方法
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
public class NatStaticMappingResolverImpl implements NatStaticMappingResolver {

    /**
     * NETCONF基本操作的入口
     */
    private RpcManager rpcManager;

    /**
     * <nat-static-mapping>层级的属性层级的属性
     */
    private String operationAttributes;

    public NatStaticMappingResolverImpl(RpcManager rpcManager) {
        this(rpcManager, null);
    }

    public NatStaticMappingResolverImpl(RpcManager rpcManager, Map<String, String> operationAttrMap) {
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
    public boolean createNatStaticMapping(NatStaticMapping newNatStaticMapping) throws IOException {
        if(newNatStaticMapping == null){
            throw new IllegalArgumentException("Parameter 'newNatStaticMapping' cannot be empty.");
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(newNatStaticMapping);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForNatStaticMapping(xmlStr);

        //2. 在 static-mapping 层级添加「create」标识
        xmlStr = xmlStr.replaceAll("<static-mapping>", "<static-mapping" + this.addOperationNamespace(OperationEnums.CREATE) + ">");

        //3. 创建静态映射
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否创建成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean editNatStaticMapping(NatStaticMapping natStaticMapping) throws IOException {
        if(natStaticMapping == null){
            throw new IllegalArgumentException("Parameter 'natStaticMapping' cannot be empty.");
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(natStaticMapping);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForNatStaticMapping(xmlStr);

        //2. 在 static-mapping 层级添加「replace」标识
        xmlStr = xmlStr.replaceAll("<static-mapping>", "<static-mapping" + this.addOperationNamespace(OperationEnums.REPLACE) + ">");

        //3. 修改静态映射
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否修改成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public NatStaticMapping getNatStaticMapping(NatStaticMapping filter) throws IOException {
        if(filter == null){
            filter = new NatStaticMapping();
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(filter);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForNatStaticMapping(xmlStr);

        //2. 查看静态映射
        String rpcReply = this.rpcManager.getConfig(TargetEnums.RUNNING, xmlStr);

        //3. 返回静态映射相关数据
        return this.parseTheReturnResult(rpcReply);
    }

    @Override
    public boolean deleteNatStaticMapping(NatStaticMapping natStaticMapping) throws IOException {
        if(natStaticMapping == null){
            throw new IllegalArgumentException("Parameter 'natStaticMapping' cannot be empty.");
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(natStaticMapping);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForNatStaticMapping(xmlStr);

        //2. 在 static-mapping 层级添加「delete」标识
        xmlStr = xmlStr.replaceAll("<static-mapping>", "<static-mapping" + this.addOperationNamespace(OperationEnums.DELETE) + ">");

        //3. 删除静态映射
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否删除成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }


    /**
     * 解析返回结果
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param rpcReply NETCONF Server的返回结果
     * @return net.juniper.netconf.adapter.huawei.natStaticMapping.model.NatStaticMapping
     */
    protected NatStaticMapping parseTheReturnResult(String rpcReply) throws JsonProcessingException {
        RpcReply<NatStaticMappingData> rpcReplyObj = XMLMAPPER_RESOURCES.get().readValue(rpcReply, new TypeReference<RpcReply<NatStaticMappingData>>() {
        });
        if(rpcReplyObj == null){
            return null;
        }

        NatStaticMappingData data = rpcReplyObj.getData();
        if(data == null){
            return null;
        }

        return data.getNatStaticMapping();
    }

    /**
     * 如果必要生成默认的operationAttrMap
     * @author zifangsky
     * @date 2021/2/18
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    protected Map<String, String> applyDefaultOperationAttrMapIfNecessary() {
        Map<String, String> defaultOperationAttrMap = new LinkedHashMap<>(4);

        defaultOperationAttrMap.put("xmlns", HuaWeiConstants.URN_HUAWEI_NAT_STATIC_MAPPING);
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
     * 初始化<nat-static-mapping>层级的属性
     */
    private void initOperationAttributes(Map<String, String> operationAttrMap) {
        StringBuilder attributes = new StringBuilder();
        for (Map.Entry<String, String> attribute : operationAttrMap.entrySet()) {
            attributes.append(String.format(" %1s=\"%2s\"", attribute.getKey(), attribute.getValue()));
        }

        this.operationAttributes = attributes.toString();
    }

    /**
     * 替 <nat-static-mapping> 添加 Namespace
     * @author zifangsky
     * @date 2021/2/18
     * @since 1.0.0
     * @param natStaticMappingTree &#60;nat-static-mapping&#62;转换成的xml字符串
     * @return java.lang.String
     */
    private String addNamespaceForNatStaticMapping(String natStaticMappingTree){
        return natStaticMappingTree.replace("<nat-static-mapping/>", "<nat-static-mapping></nat-static-mapping>")
                .replace("<nat-static-mapping>", "<nat-static-mapping" + this.operationAttributes + ">");
    }
}
