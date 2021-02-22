package cn.zifangsky.netconf.adapter.huawei.natServer;

import cn.zifangsky.netconf.adapter.huawei.HuaWeiConstants;
import cn.zifangsky.netconf.adapter.huawei.natServer.model.NatServer;
import cn.zifangsky.netconf.adapter.huawei.natServer.model.NatServerData;
import cn.zifangsky.netconf.adapter.huawei.natServer.model.ServerMapping;
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
import java.util.List;
import java.util.Map;

import static cn.zifangsky.netconf.core.DefaultRpcManager.XMLMAPPER_RESOURCES;

/**
 * NAT Server（服务器映射）相关方法
 *
 * @author zifangsky
 * @date 2021/2/19
 * @since 1.0.0
 */
public class NatServerResolverImpl implements NatServerResolver {
    /**
     * NETCONF基本操作的入口
     */
    private RpcManager rpcManager;

    /**
     * <nat-server>层级的属性
     */
    private String operationAttributes;


    public NatServerResolverImpl(RpcManager rpcManager) {
        this(rpcManager, null);
    }

    public NatServerResolverImpl(RpcManager rpcManager, Map<String, String> operationAttrMap) {
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
    public boolean createNatServerMapping(List<ServerMapping> serverMappingList) throws IOException {
        if(serverMappingList == null || serverMappingList.isEmpty()){
            throw new IllegalArgumentException("Parameter 'serverMappingList' cannot be empty.");
        }

        NatServer natServer = new NatServer();
        natServer.setServerMapping(serverMappingList);
        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(natServer);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForNatServer(xmlStr);

        //2. 在 server-mapping 层级添加「create」标识
        xmlStr = xmlStr.replaceAll("<server-mapping>", "<server-mapping" + this.addOperationNamespace(OperationEnums.CREATE) + ">");

        //3. 创建NAT Server
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否创建成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean editNatServerMapping(List<ServerMapping> serverMappingList) throws IOException {
        if(serverMappingList == null || serverMappingList.isEmpty()){
            throw new IllegalArgumentException("Parameter 'serverMappingList' cannot be empty.");
        }

        NatServer natServer = new NatServer();
        natServer.setServerMapping(serverMappingList);
        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(natServer);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForNatServer(xmlStr);

        //2. 在 server-mapping 层级添加「replace」标识
        xmlStr = xmlStr.replaceAll("<server-mapping>", "<server-mapping" + this.addOperationNamespace(OperationEnums.REPLACE) + ">");

        //3. 修改NAT Server
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否修改成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public List<ServerMapping> getNatServerMapping(NatServer filter) throws IOException {
        if(filter == null){
            filter = new NatServer();
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(filter);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForNatServer(xmlStr);

        //2. 查看NAT Server
        String rpcReply = this.rpcManager.getConfig(TargetEnums.RUNNING, xmlStr);

        //3. 返回NAT Server相关数据
        return this.parseTheReturnResult(rpcReply);
    }

    @Override
    public boolean deleteNatServerMapping(List<ServerMapping> serverMappingList) throws IOException {
        if(serverMappingList == null || serverMappingList.isEmpty()){
            throw new IllegalArgumentException("Parameter 'serverMappingList' cannot be empty.");
        }

        NatServer natServer = new NatServer();
        natServer.setServerMapping(serverMappingList);
        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(natServer);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForNatServer(xmlStr);

        //2. 在 server-mapping 层级添加「delete」标识
        xmlStr = xmlStr.replaceAll("<server-mapping>", "<server-mapping" + this.addOperationNamespace(OperationEnums.DELETE) + ">");

        //3. 删除NAT Server
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否删除成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }


    /**
     * 解析返回结果
     * @author zifangsky
     * @date 2021/2/20
     * @since 1.0.0
     * @param rpcReply NETCONF Server的返回结果
     * @return java.util.List<cn.zifangsky.netconf.adapter.huawei.natServer.model.ServerMapping>
     */
    protected List<ServerMapping> parseTheReturnResult(String rpcReply) throws JsonProcessingException {
        RpcReply<NatServerData> rpcReplyObj = XMLMAPPER_RESOURCES.get().readValue(rpcReply, new TypeReference<RpcReply<NatServerData>>() {
        });
        if(rpcReplyObj == null){
            return null;
        }

        NatServerData data = rpcReplyObj.getData();
        if(data == null){
            return null;
        }

        return data.getNatServer().getServerMapping();
    }

    /**
     * 如果必要生成默认的operationAttrMap
     * @author zifangsky
     * @date 2021/2/19
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    protected Map<String, String> applyDefaultOperationAttrMapIfNecessary() {
        Map<String, String> defaultOperationAttrMap = new LinkedHashMap<>(4);

        defaultOperationAttrMap.put("xmlns", HuaWeiConstants.URN_HUAWEI_NAT_SERVER);
        defaultOperationAttrMap.put("xmlns:nc", NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0);
        defaultOperationAttrMap.put("xmlns:yang", NetconfConstants.URN_IETF_XML_NS_YANG_1);
        return defaultOperationAttrMap;
    }

    /**
     * 在 server-mapping 层级添加「create/replace/delete」标识
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
     * 初始化<nat-server>层级的属性
     */
    private void initOperationAttributes(Map<String, String> operationAttrMap) {
        StringBuilder attributes = new StringBuilder();
        for (Map.Entry<String, String> attribute : operationAttrMap.entrySet()) {
            attributes.append(String.format(" %1s=\"%2s\"", attribute.getKey(), attribute.getValue()));
        }

        this.operationAttributes = attributes.toString();
    }

    /**
     * 替 <nat-server> 添加 Namespace
     * @author zifangsky
     * @date 2021/2/19
     * @since 1.0.0
     * @param natServerTree &#60;nat-server&#62;转换成的xml字符串
     * @return java.lang.String
     */
    private String addNamespaceForNatServer(String natServerTree){
        return natServerTree.replace("<nat-server/>", "<nat-server></nat-server>")
                .replace("<nat-server>", "<nat-server" + this.operationAttributes + ">");
    }
}