package net.juniper.netconf.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * &#60;error-option&#62;枚举
 * <p>更多参考：https://tools.ietf.org/html/rfc6241#section-7.2</p>
 *
 * @author zifangsky
 * @date 21/2/8
 * @since 1.0.0
 */
public enum ErrorOptionEnums {
    /**
     * stop-on-error
     */
    STOP_ON_ERROR("stop-on-error"),
    /**
     * continue-on-error
     */
    CONTINUE_ON_ERROR("continue-on-error"),
    /**
     * rollback-on-error
     */
    ROLLBACK_ON_ERROR("rollback-on-error");

    ErrorOptionEnums(String code) {
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
    public static ErrorOptionEnums fromCode(String code){
        for(ErrorOptionEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
