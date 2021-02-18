package net.juniper.netconf.adapter.huawei;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * vsys枚举
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
public enum DefaultVsysEnums {
    /**
     * public
     */
    PUBLIC("public");

    DefaultVsysEnums(String code) {
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
    public static DefaultVsysEnums fromCode(String code){
        for(DefaultVsysEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
