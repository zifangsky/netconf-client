package cn.zifangsky.netconf.adapter.huawei.rm.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 路由表
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
public class Route {
    /**
     * 目的地址前缀，比如：5.9.8.1
     */
    @JacksonXmlProperty(localName = "prefix")
    private String prefix;

    /**
     * 目的地址掩码，比如：32
     */
    @JacksonXmlProperty(localName = "maskLength")
    private Integer maskLength;

    /**
     * 协议，比如：Static、UNR、Direct、BGP、OSPF、RIP
     */
    @JacksonXmlProperty(localName = "protocolId")
    private String protocolId;

    /**
     * 出接口，比如：InLoopBack0
     */
    @JacksonXmlProperty(localName = "ifName")
    private String ifName;

    /**
     * 下一跳，比如：127.0.0.1
     */
    @JacksonXmlProperty(localName = "nextHop")
    private String nextHop;

    /**
     * 优先级，比如：61
     */
    @JacksonXmlProperty(localName = "preference")
    private Integer preference;

    /**
     * 开销，比如：0
     */
    @JacksonXmlProperty(localName = "cost")
    private Integer cost;
}
