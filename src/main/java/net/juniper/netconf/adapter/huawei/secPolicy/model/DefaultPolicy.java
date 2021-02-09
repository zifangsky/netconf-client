package net.juniper.netconf.adapter.huawei.secPolicy.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 默认安全策略
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
public class DefaultPolicy {
    /**
     * 表示默认安全策略的动作，取值"true"表示放行报文，取值"false"表示阻断报文。默认值是阻断。
     */
    @JacksonXmlProperty(localName = "action")
    private String action;

    /**
     * 表示默认安全策略的策略命中日志开关，取值"true"表示记录，取值"false"表示不记录。默认值是不记录。
     */
    @JacksonXmlProperty(localName = "policylog")
    private Boolean policyLog;

    /**
     * 表示默认安全策略的会话日志开关，取值"true"表示记录，取值"false"表示不记录。默认值是不记录。
     */
    @JacksonXmlProperty(localName = "sessionlog")
    private Boolean sessionLog;

    /**
     * TODO：接口文档没有该字段含义
     */
    @JacksonXmlProperty(localName = "global-ip")
    private GlobalIp globalIp;


    /* 其他内部属性 */

    @Data
    public static class GlobalIp {
        @JacksonXmlProperty(localName = "destination")
        private Boolean destination;
    }
}
