package cn.zifangsky.netconf.adapter.huawei;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Action
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 */
public enum ActionEnums {
    /**
     * 取值"true"，表示允许匹配该规则的流量通过
     */
    TRUE("true", "允许匹配该规则的流量通过"),
    /**
     * 取值"false"，表示禁止匹配该规则的流量通过
     */
    FALSE("false", "禁止匹配该规则的流量通过"),
    /**
     * 如果该动作未赋值，则该策略规则不生效
     */
    NOT_ACTIVE("", "策略规则不生效")
    ;

    ActionEnums(String code, String description) {
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
    public static ActionEnums fromCode(String code){
        for(ActionEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
