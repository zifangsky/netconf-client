package cn.zifangsky.netconf.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 协议ID枚举
 * <p>更多信息可以参考：https://zh.wikipedia.org/wiki/IP%E5%8D%8F%E8%AE%AE%E5%8F%B7%E5%88%97%E8%A1%A8</p>
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
public enum ProtocolEnums {
    /**
     * ICMP
     */
    ICMP(1, "ICMP"),
    /**
     * IGMP
     */
    IGMP(2, "IGMP"),
    /**
     * GGP
     */
    GGP(3, "GGP"),
    /**
     * IPv4
     */
    IP_V4(4, "IPv4"),
    /**
     * ST
     */
    ST(5, "ST"),
    /**
     * TCP
     */
    TCP(6, "TCP"),
    /**
     * CBT
     */
    CBT(7, "CBT"),
    /**
     * EGP
     */
    EGP(8, "EGP"),
    /**
     * IGP
     */
    IGP(9, "IGP"),
    /**
     * UDP
     */
    UDP(17, "UDP"),
    /**
     * RDP
     */
    RDP(27, "RDP"),
    /**
     * IPv6
     */
    IP_V6(41, "IPv6"),
    /**
     * GRE
     */
    GRE(47, "GRE"),
    /**
     * ESP
     */
    ESP(50, "ESP"),
    /**
     * AH
     */
    AH(51, "AH"),
    /**
     * ICMPv6
     */
    ICMP_V6(58, "ICMPv6"),
    /**
     * EIGRP
     */
    EIGRP(88, "EIGRP"),
    /**
     * OSPF
     */
    OSPF(89, "OSPF"),
    /**
     * VRRP
     */
    VRRP(112, "VRRP"),
    /**
     * L2TP
     */
    L2TP(115, "L2TP"),
    /**
     * SCTP
     */
    SCTP(132, "SCTP"),
    ;

    ProtocolEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * CODE
     */
    private int code;

    private String name;

    @JsonValue
    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @JsonCreator
    public static ProtocolEnums fromCode(Integer code){
        if(code == null){
            return null;
        }

        for(ProtocolEnums e : values()){
            if(e.code == code){
                return e;
            }
        }
        return null;
    }

    public static ProtocolEnums fromName(String name){
        for(ProtocolEnums e : values()){
            if(e.name.equals(name)){
                return e;
            }
        }
        return null;
    }
}