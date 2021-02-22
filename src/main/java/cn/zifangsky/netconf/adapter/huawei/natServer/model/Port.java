package cn.zifangsky.netconf.adapter.huawei.natServer.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示NAT Server对外/内提供的端口实例，包含起始端口，目的端口
 *
 * @author zifangsky
 * @date 2021/2/19
 * @since 1.0.0
 */
@Data
public class Port {
    /**
     * 表示NAT Server对外/内提供的起始端口。
     */
    @JacksonXmlProperty(localName = "start-port")
    private Integer startPort;

    /**
     * 表示NAT Server对外/内提供的结束端口。
     */
    @JacksonXmlProperty(localName = "end-port")
    private Integer endPort;

    public Port() {
    }

    public Port(Integer startPort) {
        this(startPort, null);
    }

    public Port(Integer startPort, Integer endPort) {
        this.startPort = startPort;
        this.endPort = endPort;
    }
}
