package net.juniper.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示用来排除参与静态映射的公网端口段。
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@Data
public class ExcludePort {
    /**
     * 表示排除端口下的起始端口，整数形式，取值范围为256～65535。
     */
    @JacksonXmlProperty(localName = "start-port")
    private Integer startPort;

    /**
     * 表示排除端口下的结束端口，整数形式，取值范围为256～65535。
     */
    @JacksonXmlProperty(localName = "end-port")
    private Integer endPort;

    public ExcludePort() {
    }

    public ExcludePort(Integer startPort, Integer endPort) {
        this.startPort = startPort;
        this.endPort = endPort;
    }
}
