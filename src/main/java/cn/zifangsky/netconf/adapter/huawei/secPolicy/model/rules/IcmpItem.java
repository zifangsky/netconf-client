package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略中直接引用ICMP协议。
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 */
@Data
public class IcmpItem {
    /**
     * 表示指定ICMP类型名。是预定义的枚举名称，比如echo、host-redirect等。
     */
    @JacksonXmlProperty(localName = "icmp-name")
    private String icmpName;

    /**
     * 表示指定ICMP节点，该节点仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "icmp-type-code")
    private IcmpTypeCode icmpTypeCode;

    public IcmpItem() {
    }

    public IcmpItem(String icmpName, IcmpTypeCode icmpTypeCode) {
        this.icmpName = icmpName;
        this.icmpTypeCode = icmpTypeCode;
    }
}
