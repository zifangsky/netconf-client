package cn.zifangsky.netconf.adapter.huawei.secPolicy.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 虚拟系统
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
public class VirtualSystem {
    /**
     * 虚拟系统的名称
     */
    @JacksonXmlProperty(localName = "name")
    private String name;

    /**
     * 表示默认安全策略，当无法匹配该vsys下所有其它安全策略时，执行默认安全策略的动作和配置。
     */
    @JacksonXmlProperty(localName = "default-policy")
    private DefaultPolicy defaultPolicy;

    /**
     * 表示静态安全策略，区别于默认安全策略，包含的子节点是用户创建出来的安全策略rule。
     */
    @JacksonXmlProperty(localName = "static-policy")
    private StaticPolicy staticPolicy;

    /**
     * 静态安全策略组
     */
    @JacksonXmlProperty(localName = "static-policy-group")
    private StaticPolicyGroup staticPolicyGroup;
}
