package cn.zifangsky.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 地址转换模式
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
public enum ModeEnums {
    /**
     * full-cone
     */
    FULL_CONE("full-cone");

    ModeEnums(String code) {
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
    public static ModeEnums fromCode(String code){
        for(ModeEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
