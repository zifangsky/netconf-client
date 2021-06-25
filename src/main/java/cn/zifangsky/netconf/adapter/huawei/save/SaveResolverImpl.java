package cn.zifangsky.netconf.adapter.huawei.save;

import cn.zifangsky.netconf.adapter.huawei.DefaultVsysEnums;
import cn.zifangsky.netconf.adapter.huawei.HuaWeiConstants;
import cn.zifangsky.netconf.adapter.huawei.save.model.Save;
import cn.zifangsky.netconf.core.NetconfConstants;
import cn.zifangsky.netconf.core.RpcManager;
import cn.zifangsky.netconf.core.enums.TargetEnums;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static cn.zifangsky.netconf.core.AbstractExecutingRpcManager.XMLMAPPER_RESOURCES;

/**
 * 配置保存相关方法
 *
 * @author zifangsky
 * @date 2021/6/25
 * @since 1.0.0
 */
public class SaveResolverImpl implements SaveResolver {

    /**
     * NETCONF基本操作的入口
     */
    private RpcManager rpcManager;

    /**
     * <save>层级的属性
     */
    private String operationAttributes;

    public SaveResolverImpl(RpcManager rpcManager) {
        this(rpcManager, null);
    }

    public SaveResolverImpl(RpcManager rpcManager, Map<String, String> operationAttrMap) {
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
    public boolean save(String fileName) throws IOException {
        return this.save(DefaultVsysEnums.PUBLIC.getCode(), fileName);
    }

    @Override
    public boolean save(String vsys, String fileName) throws IOException {
        if(StringUtils.isBlank(vsys) || StringUtils.isBlank(fileName)){
            return false;
        }

        Save save = new Save(vsys, fileName);
        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(save);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForSecurityZone(xmlStr);

        //2. 保存配置
        String rpcReply = this.rpcManager.getConfig(TargetEnums.RUNNING, xmlStr);

        //3. 返回执行结果
        return this.rpcManager.checkIsSuccess(rpcReply);
    }


    /**
     * 如果必要生成默认的operationAttrMap
     * @author zifangsky
     * @date 2021/6/25
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    protected Map<String, String> applyDefaultOperationAttrMapIfNecessary() {
        Map<String, String> defaultOperationAttrMap = new LinkedHashMap<>(4);

        defaultOperationAttrMap.put("xmlns", HuaWeiConstants.URN_HUAWEI_SYSTEM);
        defaultOperationAttrMap.put("xmlns:nc", NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0);
        defaultOperationAttrMap.put("xmlns:yang", NetconfConstants.URN_IETF_XML_NS_YANG_1);
        return defaultOperationAttrMap;
    }

    /**
     * 初始化<save>层级的属性
     */
    private void initOperationAttributes(Map<String, String> operationAttrMap) {
        StringBuilder attributes = new StringBuilder();
        for (Map.Entry<String, String> attribute : operationAttrMap.entrySet()) {
            attributes.append(String.format(" %1s=\"%2s\"", attribute.getKey(), attribute.getValue()));
        }

        this.operationAttributes = attributes.toString();
    }

    /**
     * 替 <save> 添加 Namespace
     * @author zifangsky
     * @date 2021/6/25
     * @since 1.0.0
     * @param saveTree &#60;save&#62;转换成的xml字符串
     * @return java.lang.String
     */
    private String addNamespaceForSecurityZone(String saveTree){
        return saveTree.replace("<save/>", "<save></save>")
                .replaceFirst("<save>", "<save" + this.operationAttributes + ">");
    }
}