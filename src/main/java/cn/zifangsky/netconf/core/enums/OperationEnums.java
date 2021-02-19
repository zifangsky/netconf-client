package cn.zifangsky.netconf.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * &#60;edit-config&#62;可以执行的操作
 * <p>更多参考：https://tools.ietf.org/html/rfc6241#section-7.2</p>
 *
 * @author zifangsky
 * @date 21/2/8
 * @since 1.0.0
 */
public enum OperationEnums {
    /**
     * merge
     */
    MERGE("merge"),
    /**
     * replace
     */
    REPLACE("replace"),
    /**
     * create
     */
    CREATE("create"),
    /**
     * delete
     */
    DELETE("delete"),
    /**
     * remove
     */
    REMOVE("remove");

    OperationEnums(String code) {
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
    public static OperationEnums fromCode(String code){
        for(OperationEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
