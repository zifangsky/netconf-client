package cn.zifangsky.netconf.adapter.huawei.securityZone;

import cn.zifangsky.netconf.adapter.huawei.HuaWeiConstants;
import cn.zifangsky.netconf.adapter.huawei.securityZone.model.SecurityZone;
import cn.zifangsky.netconf.adapter.huawei.securityZone.model.SecurityZoneData;
import cn.zifangsky.netconf.adapter.huawei.securityZone.model.ZoneInstance;
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

import static cn.zifangsky.netconf.core.AbstractExecutingRpcManager.XMLMAPPER_RESOURCES;

/**
 * 安全区域相关方法
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
public class SecurityZoneResolverImpl implements SecurityZoneResolver {
    /**
     * NETCONF基本操作的入口
     */
    private RpcManager rpcManager;

    /**
     * <security-zone>层级的属性
     */
    private String operationAttributes;


    public SecurityZoneResolverImpl(RpcManager rpcManager) {
        this(rpcManager, null);
    }

    public SecurityZoneResolverImpl(RpcManager rpcManager, Map<String, String> operationAttrMap) {
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
    public boolean createSecurityZone(List<ZoneInstance> zoneInstanceList) throws IOException {
        if(zoneInstanceList == null || zoneInstanceList.isEmpty()){
            throw new IllegalArgumentException("Parameter 'zoneInstanceList' cannot be empty.");
        }

        SecurityZone securityZone = new SecurityZone(zoneInstanceList);
        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(securityZone);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecurityZone(xmlStr);

        //2. 在 zone-instance 层级添加「create」标识
        xmlStr = xmlStr.replaceAll("<zone-instance>", "<zone-instance" + this.addOperationNamespace(OperationEnums.CREATE) + ">");

        //3. 创建安全区域
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否创建成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public boolean editSecurityZone(List<ZoneInstance> zoneInstanceList) throws IOException {
        if(zoneInstanceList == null || zoneInstanceList.isEmpty()){
            throw new IllegalArgumentException("Parameter 'serverMappingList' cannot be empty.");
        }

        SecurityZone securityZone = new SecurityZone(zoneInstanceList);
        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(securityZone);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecurityZone(xmlStr);

        //2. 在 zone-instance 层级添加「replace」标识
        xmlStr = xmlStr.replaceAll("<zone-instance>", "<zone-instance" + this.addOperationNamespace(OperationEnums.REPLACE) + ">");

        //3. 修改安全区域
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否修改成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }

    @Override
    public List<ZoneInstance> getSecurityZone(SecurityZone filter) throws IOException {
        if(filter == null){
            filter = new SecurityZone();
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(filter);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecurityZone(xmlStr);

        //2. 查看安全区域
        String rpcReply = this.rpcManager.getConfig(TargetEnums.RUNNING, xmlStr);

        //3. 返回安全区域相关数据
        return this.parseTheReturnResult(rpcReply);
    }

    @Override
    public boolean deleteSecurityZone(List<ZoneInstance> zoneInstanceList) throws IOException {
        if(zoneInstanceList == null || zoneInstanceList.isEmpty()){
            throw new IllegalArgumentException("Parameter 'zoneInstanceList' cannot be empty.");
        }

        SecurityZone securityZone = new SecurityZone(zoneInstanceList);
        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(securityZone);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecurityZone(xmlStr);

        //2. 在 zone-instance 层级添加「delete」标识
        xmlStr = xmlStr.replaceAll("<zone-instance>", "<zone-instance" + this.addOperationNamespace(OperationEnums.DELETE) + ">");

        //3. 删除安全区域
        String rpcReply = this.rpcManager.editConfig(TargetEnums.RUNNING, null, null, ErrorOptionEnums.ROLLBACK_ON_ERROR, xmlStr);

        //4. 返回是否删除成功
        return this.rpcManager.checkIsSuccess(rpcReply);
    }


    /**
     * 解析返回结果
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param rpcReply NETCONF Server的返回结果
     * @return java.util.List<cn.zifangsky.netconf.adapter.huawei.securityZone.model.ZoneInstance>
     */
    protected List<ZoneInstance> parseTheReturnResult(String rpcReply) throws JsonProcessingException {
        RpcReply<SecurityZoneData> rpcReplyObj = XMLMAPPER_RESOURCES.get().readValue(rpcReply, new TypeReference<RpcReply<SecurityZoneData>>() {
        });
        if(rpcReplyObj == null){
            return null;
        }

        SecurityZoneData data = rpcReplyObj.getData();
        if(data == null){
            return null;
        }

        return data.getSecurityZone().getZoneInstance();
    }

    /**
     * 如果必要生成默认的operationAttrMap
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    protected Map<String, String> applyDefaultOperationAttrMapIfNecessary() {
        Map<String, String> defaultOperationAttrMap = new LinkedHashMap<>(4);

        defaultOperationAttrMap.put("xmlns", HuaWeiConstants.URN_HUAWEI_SECURITY_ZONE);
        defaultOperationAttrMap.put("xmlns:nc", NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0);
        defaultOperationAttrMap.put("xmlns:yang", NetconfConstants.URN_IETF_XML_NS_YANG_1);
        return defaultOperationAttrMap;
    }

    /**
     * 在 zone-instance 层级添加「create/replace/delete」标识
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
     * 初始化<security-zone>层级的属性
     */
    private void initOperationAttributes(Map<String, String> operationAttrMap) {
        StringBuilder attributes = new StringBuilder();
        for (Map.Entry<String, String> attribute : operationAttrMap.entrySet()) {
            attributes.append(String.format(" %1s=\"%2s\"", attribute.getKey(), attribute.getValue()));
        }

        this.operationAttributes = attributes.toString();
    }

    /**
     * 替 <security-zone> 添加 Namespace
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param securityZoneTree &#60;security-zone&#62;转换成的xml字符串
     * @return java.lang.String
     */
    private String addNamespaceForSecurityZone(String securityZoneTree){
        return securityZoneTree.replace("<security-zone/>", "<security-zone></security-zone>")
                .replace("<security-zone>", "<security-zone" + this.operationAttributes + ">");
    }
}