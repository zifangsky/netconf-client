package cn.zifangsky.netconf.adapter.huawei.natServer.model;

import cn.zifangsky.netconf.adapter.huawei.ProtocolEnums;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示NAT Server实例
 *
 * @author zifangsky
 * @date 2021/2/19
 * @since 1.0.0
 */
@Data
public class ServerMapping {
    /**
     * 字符串形式，表示NAT Server的名字，长度为1~31字节。
     */
    @JacksonXmlProperty(localName = "name")
    private String name;

    /**
     * 表示该NAT Server属于的虚系统，如果属于根系统，则使用public，具体约束和虚系统名称约束相同
     */
    @JacksonXmlProperty(localName = "vsys")
    private String vsys;

    /**
     * 表示NAT Server公网IP地址所在的VPN实例的名称
     */
    @JacksonXmlProperty(localName = "global-vpn-name")
    private String globalVpnName;

    /**
     * 表示承载的协议类型或者协议号，目前支持的类型，主要包含TCP，UDP，ICMP，41, 47, 50, 51。
     */
    @JacksonXmlProperty(localName = "protocol")
    private ProtocolEnums protocol;

    /**
     * 表示NAT Server对外提供的地址实例，可以是某个接口的IP或者配置的IP地址段，仅用于容纳子节点，自身数据没有意义。
     */
    @JacksonXmlProperty(localName = "global")
    private Global global;

    /**
     * 表示NAT Server对外提供的端口实例，包含起始端口，目的端口，仅用于容纳子节点，自身数据没有意义。
     */
    @JacksonXmlProperty(localName = "global-port")
    private Port globalPort;

    /**
     * 表示NAT Server对外提供的地址实例，可以是某个接口的IP或者配置的IP地址段，仅用于容纳子节点，自身数据没有意义。
     */
    @JacksonXmlProperty(localName = "inside")
    private Inside inside;

    /**
     * 表示NAT Server对内提供的端口实例，包含起始端口，目的端口，仅用于容纳子节点，自身数据没有意义。
     */
    @JacksonXmlProperty(localName = "inside-port")
    private Port insidePort;

    /**
     * True表示内部服务器无法主动访问外部，False表示内部服务器可以主动访问外部网络。
     */
    @JacksonXmlProperty(localName = "no-reverse")
    private Boolean noReverse;

    /**
     * 表示NAT Server 内部服务器所在的VPN实例的名称
     */
    @JacksonXmlProperty(localName = "inside-vpn-name")
    private String insideVpnName;

    public ServerMapping() {
    }

    public ServerMapping(String name, String vsys) {
        this.name = name;
        this.vsys = vsys;
    }

    public ServerMapping(String name, String vsys, ProtocolEnums protocol, Global global, Port globalPort, Inside inside, Port insidePort) {
        this(name, vsys, null, protocol, global, globalPort, inside, insidePort, true, null);
    }

    public ServerMapping(String name, String vsys, String globalVpnName, ProtocolEnums protocol, Global global, Port globalPort, Inside inside, Port insidePort, Boolean noReverse, String insideVpnName) {
        this.name = name;
        this.vsys = vsys;
        this.globalVpnName = globalVpnName;
        this.protocol = protocol;
        this.global = global;
        this.globalPort = globalPort;
        this.inside = inside;
        this.insidePort = insidePort;
        this.noReverse = noReverse;
        this.insideVpnName = insideVpnName;
    }
}
