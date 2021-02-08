package net.juniper.netconf.adapter.huawei.secPolicy.rule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>表示安全策略规则引用的服务信息，该节点仅用于容纳子节点，自身无数据意义，一条安全规则中最多只能出现1次。</p>
 * <p>该节点的子节点可以是服务、服务组对象，也可以指定服务规则（协议、端口等）</p>
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
public class Service {
    /**
     * 表示安全策略规则引用服务（组）对象，通过名称引用。
     */
    @JacksonXmlProperty(localName = "service-object")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> serviceObject;

    /**
     * 表示安全策略中直接引用的协议节点，可以是TCP/UDP/SCTP协议、ICMP/ICMP6协议或其它IP层协议。该节点仅用于容纳子节点，自身无数据意义，一条安全规则中最多只能出现1次。
     */
    @JacksonXmlProperty(localName = "service-items")
    private ServiceItems serviceItems;

    /**
     * 表示在安全策略服务条件中排除某些协议，可以排除TCP/UDP/SCTP协议、ICMP/ICMP6协议或其它IP层协议。
     */
    @JacksonXmlProperty(localName = "service-items-exclude")
    private ServiceItemsExclude serviceItemsExclude;
}
