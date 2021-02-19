package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略中直接通过协议ID引用协议，该节点仅用于容纳子节点，自身无数据意义。
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 */
@Data
public class Protocol {
    /**
     * 表示安全策略中引用的协议ID，取值范围0-255。
     */
    @JacksonXmlProperty(localName = "protocol-id")
    private Integer protocolId;

    public Protocol() {
    }

    public Protocol(Integer protocolId) {
        this.protocolId = protocolId;
    }
}
