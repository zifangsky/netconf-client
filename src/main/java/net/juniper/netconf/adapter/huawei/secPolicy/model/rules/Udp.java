package net.juniper.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略中直接引用UDP协议。
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 */
@Data
public class Udp {
    /**
     * 表示指定UDP的源端口。如<source-port>100 200 to 300</source-port>，表示指定源端口为100、200到300之间所有端口。
     */
    @JacksonXmlProperty(localName = "source-port")
    private String sourcePort;

    /**
     * 表示指定UDP的目的端口。如<dest-port>300 400 to 500 1023</dest-port>，表示指定源端口为300、400到500之间端口、1023。
     */
    @JacksonXmlProperty(localName = "dest-port")
    private String destPort;

    public Udp() {
    }

    public Udp(String sourcePort, String destPort) {
        this.sourcePort = sourcePort;
        this.destPort = destPort;
    }
}
