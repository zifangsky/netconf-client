package cn.zifangsky.netconf.adapter.huawei.rm;

import cn.zifangsky.netconf.adapter.huawei.HuaWeiConstants;
import cn.zifangsky.netconf.adapter.huawei.rm.model.Rm;
import cn.zifangsky.netconf.adapter.huawei.rm.model.RmData;
import cn.zifangsky.netconf.adapter.huawei.rm.model.Route;
import cn.zifangsky.netconf.core.NetconfConstants;
import cn.zifangsky.netconf.core.RpcManager;
import cn.zifangsky.netconf.core.model.RpcReply;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.zifangsky.netconf.core.DefaultRpcManager.XMLMAPPER_RESOURCES;

/**
 * 路由状态相关方法
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
public class RmResolverImpl implements RmResolver {
    /**
     * NETCONF基本操作的入口
     */
    private RpcManager rpcManager;

    /**
     * <rm>层级的属性
     */
    private String operationAttributes;


    public RmResolverImpl(RpcManager rpcManager) {
        this(rpcManager, null);
    }

    public RmResolverImpl(RpcManager rpcManager, Map<String, String> operationAttrMap) {
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
    public List<Route> getRouteState(Rm filter) throws IOException {
        if(filter == null){
            filter = new Rm();
        }

        String xmlStr = XMLMAPPER_RESOURCES.get().writeValueAsString(filter);
        //1. 添加 namespace
        xmlStr = this.addNamespaceForRm(xmlStr);

        //2. 查看路由状态
        String rpcReply = this.rpcManager.get(xmlStr);

        //3. 返回路由状态相关数据
        return this.parseTheReturnResult(rpcReply);
    }


    /**
     * 解析返回结果
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param rpcReply NETCONF Server的返回结果
     * @return java.util.List<cn.zifangsky.netconf.adapter.huawei.rm.model.Route>
     */
    protected List<Route> parseTheReturnResult(String rpcReply) throws JsonProcessingException {
        RpcReply<RmData> rpcReplyObj = XMLMAPPER_RESOURCES.get().readValue(rpcReply, new TypeReference<RpcReply<RmData>>() {
        });
        if(rpcReplyObj == null){
            return null;
        }

        RmData data = rpcReplyObj.getData();
        if(data == null){
            return null;
        }

        return data.getRm().getRmBase().getUniAf().get(0).getTopology().get(0).getRoute();
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

        defaultOperationAttrMap.put("xmlns", HuaWeiConstants.HUAWEI_RM);
        defaultOperationAttrMap.put("xmlns:nc", NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0);
        defaultOperationAttrMap.put("xmlns:yang", NetconfConstants.URN_IETF_XML_NS_YANG_1);
        return defaultOperationAttrMap;
    }

    /**
     * 初始化<rm>层级的属性
     */
    private void initOperationAttributes(Map<String, String> operationAttrMap) {
        StringBuilder attributes = new StringBuilder();
        for (Map.Entry<String, String> attribute : operationAttrMap.entrySet()) {
            attributes.append(String.format(" %1s=\"%2s\"", attribute.getKey(), attribute.getValue()));
        }

        this.operationAttributes = attributes.toString();
    }

    /**
     * 替 <rm> 添加 Namespace
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param rmTree &#60;rm&#62;转换成的xml字符串
     * @return java.lang.String
     */
    private String addNamespaceForRm(String rmTree){
        return rmTree.replace("<rm/>", "<rm></rm>")
                .replace("<rm>", "<rm" + this.operationAttributes + ">");
    }
}
