package net.juniper.netconf.adapter.huawei.secPolicy.rule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示指定ICMP/ICMP6节点，该节点仅用于容纳子节点，自身无数据意义。子节点需要包含报文类型号和消息码两个字段。
 */
@Data
public class IcmpTypeCode {
    /**
     * 表示指定ICMP/ICMP6报文类型，取值范围0-255。
     */
    @JacksonXmlProperty(localName = "icmp-type-number")
    private Integer icmpTypeNumber;

    /**
     * 表示指定ICMP/ICMP6消息码，取值范围0-255。
     */
    @JacksonXmlProperty(localName = "icmp-code-number")
    private Integer icmpCodeNumber;
}
