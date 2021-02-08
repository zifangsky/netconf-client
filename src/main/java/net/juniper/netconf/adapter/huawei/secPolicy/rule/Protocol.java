package net.juniper.netconf.adapter.huawei.secPolicy.rule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略中直接通过协议ID引用协议，该节点仅用于容纳子节点，自身无数据意义。
 */
@Data
public class Protocol {
    /**
     * 表示安全策略中引用的协议ID，取值范围0-255。
     */
    @JacksonXmlProperty(localName = "protocol-id")
    private Integer protocolId;
}
