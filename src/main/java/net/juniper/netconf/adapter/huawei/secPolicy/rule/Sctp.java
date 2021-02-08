package net.juniper.netconf.adapter.huawei.secPolicy.rule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略中直接引用SCTP协议。
 */
@Data
public class Sctp {
    /**
     * 表示指定SCTP的源端口。如<source-port>100 200 to 300</source-port>，表示指定源端口为100、200到300之间所有端口。
     */
    @JacksonXmlProperty(localName = "source-port")
    private String sourcePort;

    /**
     * 表示指定SCTP的目的端口。如<dest-port>300 400 to 500 1023</dest-port>，表示指定源端口为300、400到500之间端口、1023。
     */
    @JacksonXmlProperty(localName = "dest-port")
    private String destPort;
}
