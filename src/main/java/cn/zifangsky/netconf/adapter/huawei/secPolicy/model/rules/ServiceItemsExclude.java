package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * <p>表示在安全策略服务条件中排除某些协议，可以排除TCP/UDP/SCTP协议、ICMP/ICMP6协议或其它IP层协议。</p>
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
public class ServiceItemsExclude {
    /**
     * 表示安全策略中直接引用TCP协议。
     */
    @JacksonXmlProperty(localName = "tcp")
    private Tcp tcp;

    /**
     * 表示安全策略中直接引用UDP协议。
     */
    @JacksonXmlProperty(localName = "udp")
    private Udp udp;

    /**
     * 表示安全策略中直接引用SCTP协议。
     */
    @JacksonXmlProperty(localName = "sctp")
    private Sctp sctp;

    /**
     * 表示安全策略中直接引用ICMP协议。
     */
    @JacksonXmlProperty(localName = "icmp-item")
    private IcmpItem icmpItem;

    /**
     * 表示安全策略中直接引用ICMPV6协议。
     */
    @JacksonXmlProperty(localName = "icmp6-item")
    private Icmp6Item icmp6Item;

    /**
     * 表示安全策略中直接通过协议ID引用协议，该节点仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "protocol")
    private Protocol protocol;
}
