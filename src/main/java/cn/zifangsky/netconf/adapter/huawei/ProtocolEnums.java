package cn.zifangsky.netconf.adapter.huawei;

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
    ICMP(1),
    /**
     * IGMP
     */
    IGMP(2),
    /**
     * GGP
     */
    GGP(3),
    /**
     * IPv4
     */
    IP_V4(4),
    /**
     * ST
     */
    ST(5),
    /**
     * TCP
     */
    TCP(6),
    /**
     * CBT
     */
    CBT(7),
    /**
     * EGP
     */
    EGP(8),
    /**
     * IGP
     */
    IGP(9),
    /**
     * UDP
     */
    UDP(17),
    /**
     * RDP
     */
    RDP(27),
    /**
     * IPv6
     */
    IP_V6(41),
    /**
     * GRE
     */
    GRE(47),
    /**
     * ESP
     */
    ESP(50),
    /**
     * AH
     */
    AH(51),
    /**
     * ICMPv6
     */
    ICMP_V6(58),
    /**
     * EIGRP
     */
    EIGRP(88),
    /**
     * OSPF
     */
    OSPF(89),
    /**
     * VRRP
     */
    VRRP(112),
    /**
     * L2TP
     */
    L2TP(115),
    /**
     * SCTP
     */
    SCTP(132),
    ;

    ProtocolEnums(int code) {
        this.code = code;
    }

    /**
     * CODE
     */
    private int code;

    @JsonValue
    public int getCode() {
        return code;
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
}