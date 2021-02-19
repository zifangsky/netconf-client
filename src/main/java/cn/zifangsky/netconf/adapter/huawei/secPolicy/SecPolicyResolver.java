package cn.zifangsky.netconf.adapter.huawei.secPolicy;

import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.SecPolicy;

import java.io.IOException;

/**
 * 安全策略相关方法
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 */
public interface SecPolicyResolver {

    /**
     * 创建安全策略
     * @author zifangsky
     * @date 2021/2/9
     * @since 1.0.0
     * @param newSecPolicy 新的安全策略
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean createSecPolicy(SecPolicy newSecPolicy) throws IOException;

    /**
     * 修改安全策略（修改时会根据rule的名称匹配已有的策略规则）
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param secPolicy 安全策略
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean editSecPolicy(SecPolicy secPolicy) throws IOException;

    /**
     * 查看安全策略
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param filter 筛选条件
     * @return SecPolicy 结果
     * @throws IOException If there are errors communicating with the netconf server.
     */
    SecPolicy getSecPolicy(SecPolicy filter) throws IOException;

    /**
     * 删除安全策略（删除时只需要提供rule的名称即可）
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param secPolicy 安全策略
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean deleteSecPolicy(SecPolicy secPolicy) throws IOException;

    /**
     * 查看安全策略的命中次数
     * @author zifangsky
     * @date 2021/2/10
     * @since 1.0.0
     * @param filter 筛选条件
     * @return SecPolicy 包含命中次数的结果
     * @throws IOException If there are errors communicating with the netconf server.
     */
    SecPolicy getSecPolicyHitTimes(SecPolicy filter) throws IOException;


}
