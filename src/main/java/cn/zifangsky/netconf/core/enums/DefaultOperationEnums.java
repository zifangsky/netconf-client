package cn.zifangsky.netconf.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * &#60;default-operation&#62;枚举，该节点默认值是 merge
 * <p>更多参考：https://tools.ietf.org/html/rfc6241#section-7.2</p>
 *
 * @author zifangsky
 * @date 21/2/8
 * @since 1.0.0
 */
public enum DefaultOperationEnums {
    /**
     * merge
     */
    MERGE("merge"),
    /**
     * replace
     */
    REPLACE("replace"),
    /**
     * none
     */
    NONE("none");

    DefaultOperationEnums(String code) {
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
    public static DefaultOperationEnums fromCode(String code){
        for(DefaultOperationEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
