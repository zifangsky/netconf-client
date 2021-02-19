package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 表示在安全策略阻断流量时，向源或者目的发送反馈报文
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
public enum SendDenyPacketEnums {
    /**
     * 向源发送tcp-rst报文
     */
    TCP_RESET_TO_CLIENT("tcp-reset-to-client", "向源发送tcp-rst报文"),
    /**
     * 向目的发送tcp-rst报文
     */
    TCP_RESET_TO_SERVER("tcp-reset-to-server", "向目的发送tcp-rst报文"),
    /**
     * 向源发送icmp不可达报文
     */
    ICMP_DESTINATION_UNREACHABLE("icmp-destination-unreachable", "向源发送icmp不可达报文")
    ;

    SendDenyPacketEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * CODE
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static SendDenyPacketEnums fromCode(String code){
        for(SendDenyPacketEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
