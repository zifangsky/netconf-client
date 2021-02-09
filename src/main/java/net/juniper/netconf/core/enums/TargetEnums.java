package net.juniper.netconf.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * &#60;target&#62;节点枚举
 *
 * @author zifangsky
 * @date 21/2/8
 * @since 1.0.0
 */
public enum TargetEnums {
    /**
     * startup
     */
    STARTUP("startup"),
    /**
     * candidate
     */
    CANDIDATE("candidate"),
    /**
     * running
     */
    RUNNING("running");

    TargetEnums(String code) {
        this.code = code;
    }

    /**
     * CODE
     */
    private String code;

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static TargetEnums fromCode(String code){
        for(TargetEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}