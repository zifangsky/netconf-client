package net.juniper.netconf.adapter.huawei.secPolicy;

import net.juniper.netconf.adapter.huawei.secPolicy.model.SecPolicy;

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



}
