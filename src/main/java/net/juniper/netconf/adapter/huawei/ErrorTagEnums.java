package net.juniper.netconf.adapter.huawei;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 错误类型枚举
 *
 * @author zifangsky
 * @date 2021/2/4
 * @since 1.0.0
 */
public enum ErrorTagEnums {
    /**
     * 对象正在被使用。如删除ACL时，ACL正被IPSec引用，无法删除。
     */
    IN_USE("in-use", "对象正在被使用"),
    /**
     * 元素取值非法
     */
    INVALID_VALUE("invalid-value", "元素取值非法"),
    /**
     * 请求或应答消息超过Server端限制
     */
    TOO_BIG("too-big", "请求或应答消息超过Server端限制"),
    /**
     * 缺少元素的必须属性
     */
    MISSING_ATTRIBUTE("missing-attribute", "缺少元素的必须属性"),
    /**
     * 元素属性值非法
     */
    BAD_ATTRIBUTE("bad-attribute", "元素属性值非法"),
    /**
     * 未定义的元素属性
     */
    UNKNOWN_ATTRIBUTE("unknown-attribute", "未定义的元素属性"),
    /**
     * 缺少必须的元素
     */
    MISSING_ELEMENT("missing-element", "缺少必须的元素"),
    /**
     * 元素取值非法
     */
    BAD_ELEMENT("bad-element", "元素取值非法"),
    /**
     * 未定义的元素节点
     */
    UNKNOWN_ELEMENT("unknown-element", "未定义的元素节点"),
    /**
     * 未定义的命名空间
     */
    UNKNOWN_NAMESPACE("unknown-namespace", "未定义的命名空间"),
    /**
     * 访问被拒绝，用户无权限访问或认证失败
     */
    ACCESS_DENIED("access-denied", "访问被拒绝，用户无权限访问或认证失败"),
    /**
     * 访问的资源已经被锁定
     */
    LOCK_DENIED("lock-denied", "访问的资源已经被锁定"),
    /**
     * 请求资源失败，如超过Server端规格限制，创建超规格的安全策略等
     */
    RESOURCE_DENIED("resource-denied", "请求资源失败"),
    /**
     * Server回滚操作失败
     */
    ROLLBACK_FAILED("rollback-failed", "Server回滚操作失败"),
    /**
     * 数据已存在，如create操作创建一个已存在的对象
     */
    DATA_EXISTS("data-exists", "数据已存在"),
    /**
     * 数据不存在，如delete操作删除一个不存在的对象
     */
    DATA_MISSING("data-missing", "数据不存在"),
    /**
     * 操作不支持
     */
    OPERATION_NOT_SUPPORTED("operation-not-supported", "操作不支持"),
    /**
     * 操作失败，通用的错误描述。
     */
    OPERATION_FAILED("operation-failed", "操作失败"),
    /**
     * XML格式非法
     */
    MALFORMED_MESSAGE("malformed-message", "XML格式非法")
    ;

    ErrorTagEnums(String code, String description) {
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
    public static ErrorTagEnums fromCode(String code){
        for(ErrorTagEnums e : values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }
}
