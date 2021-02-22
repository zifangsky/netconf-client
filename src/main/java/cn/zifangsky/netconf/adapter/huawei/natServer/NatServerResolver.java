package cn.zifangsky.netconf.adapter.huawei.natServer;

import cn.zifangsky.netconf.adapter.huawei.natServer.model.NatServer;
import cn.zifangsky.netconf.adapter.huawei.natServer.model.ServerMapping;

import java.io.IOException;
import java.util.List;

/**
 * NAT Server（服务器映射）相关方法
 *
 * @author zifangsky
 * @date 2021/2/19
 * @since 1.0.0
 */
public interface NatServerResolver {
    /**
     * 创建NAT Server
     * @author zifangsky
     * @date 2021/2/9
     * @since 1.0.0
     * @param serverMappingList 新的NAT Server
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean createNatServerMapping(List<ServerMapping> serverMappingList) throws IOException;

    /**
     * 修改NAT Server（修改时会根据server-mapping的名称匹配已有的策略规则）
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param serverMappingList NAT Server
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean editNatServerMapping(List<ServerMapping> serverMappingList) throws IOException;

    /**
     * 查看NAT Server
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param filter 筛选条件
     * @return 结果
     * @throws IOException If there are errors communicating with the netconf server.
     */
    List<ServerMapping> getNatServerMapping(NatServer filter) throws IOException;

    /**
     * 删除NAT Server（删除时只需要提供server-mapping的名称即可）
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param serverMappingList 需要删除的NAT Server
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean deleteNatServerMapping(List<ServerMapping> serverMappingList) throws IOException;
}