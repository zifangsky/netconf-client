package cn.zifangsky.netconf.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * &#60;test-option&#62;枚举
 * <p>更多参考：https://tools.ietf.org/html/rfc6241#section-7.2</p>
 *
 * @author zifangsky
 * @date 21/2/8
 * @since 1.0.0
 */
public enum TestOptionEnums {
    /**
     * test-then-set
     */
    TEST_THEN_SET("test-then-set"),
    /**
     * set
     */
    SET("set"),
    /**
     * test-only
     */
    TEST_ONLY("test-only");

    TestOptionEnums(String code) {
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
    public static TestOptionEnums fromCode(String code){
        for(TestOptionEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
