package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略中直接引用ICMPV6协议。
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
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

    public Icmp6Item() {
    }

    public Icmp6Item(String icmp6Name, IcmpTypeCode icmp6TypeCode) {
        this.icmp6Name = icmp6Name;
        this.icmp6TypeCode = icmp6TypeCode;
    }
}
