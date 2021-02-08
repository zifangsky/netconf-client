package net.juniper.netconf.adapter.huawei.secPolicy.rule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略中直接引用ICMPV6协议。
 */
@Data
public class Icmp6Item {
    /**
     * 表示指定ICMP类型名。是预定义的枚举名称，比如echo、host-redirect等。
     */
    @JacksonXmlProperty(localName = "icmp6-name")
    private String icmp6Name;

    /**
     * 表示指定ICMP节点，该节点仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "icmp6-type-code")
    private IcmpTypeCode icmp6TypeCode;
}
