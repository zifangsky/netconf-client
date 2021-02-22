package cn.zifangsky.netconf.adapter.huawei.natServer.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * NAT Server（服务器映射）
 *
 * @author zifangsky
 * @date 2021/2/19
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "nat-server")
public class NatServer {
    /**
     * 表示NAT Server实例，仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "server-mapping")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ServerMapping> serverMapping;

    public NatServer() {
    }

    public NatServer(List<ServerMapping> serverMapping) {
        this.serverMapping = serverMapping;
    }
}
