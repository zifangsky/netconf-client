package cn.zifangsky.netconf.adapter.huawei.natStaticMapping;

import cn.zifangsky.netconf.adapter.huawei.natStaticMapping.model.NatStaticMapping;

import java.io.IOException;

/**
 * 静态映射相关方法
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
public interface NatStaticMappingResolver {
    /**
     * 创建静态映射
     * @author zifangsky
     * @date 2021/2/9
     * @since 1.0.0
     * @param newNatStaticMapping 新的静态映射
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean createNatStaticMapping(NatStaticMapping newNatStaticMapping) throws IOException;

    /**
     * 修改静态映射（修改时会根据static-mapping的名称匹配已有的映射规则）
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param natStaticMapping 静态映射
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean editNatStaticMapping(NatStaticMapping natStaticMapping) throws IOException;

    /**
     * 查看静态映射
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param filter 筛选条件
     * @return 结果
     * @throws IOException If there are errors communicating with the netconf server.
     */
    NatStaticMapping getNatStaticMapping(NatStaticMapping filter) throws IOException;

    /**
     * 删除静态映射（删除时只需要提供rule的名称即可）
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param natStaticMapping 静态映射
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean deleteNatStaticMapping(NatStaticMapping natStaticMapping) throws IOException;
}
