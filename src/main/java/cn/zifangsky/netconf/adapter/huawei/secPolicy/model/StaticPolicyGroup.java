package cn.zifangsky.netconf.adapter.huawei.secPolicy.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * 静态安全策略组
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
public class StaticPolicyGroup {
    /**
     * 表示策略组，仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "group")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Group> group;

    /* 其他内部属性 */
    /**
     * 一个静态安全策略组
     */
    @Data
    public static class Group {
        /**
         * 表示安全策略组的名称
         */
        @JacksonXmlProperty(localName = "name")
        private String name;

        /**
         * 表示安全策略组的使能状态，取值"true"表示使能，取值"false"表示不使能。
         */
        @JacksonXmlProperty(localName = "enable")
        private Boolean enable;

        /**
         * 表示安全策略组的描述信息
         */
        @JacksonXmlProperty(localName = "description")
        private String desc;

        /**
         * 表示策略组范围，仅用于包含子节点，自身无数据意义。
         */
        @JacksonXmlProperty(localName = "rule-range")
        private RuleRange ruleRange;
    }

    /**
     * 表示策略组范围，仅用于包含子节点，自身无数据意义。
     */
    @Data
    public static class RuleRange {
        /**
         * 表示策略组起始规则。
         */
        @JacksonXmlProperty(localName = "start-rule")
        private String startRule;

        /**
         * 表示策略组结束规则。
         */
        @JacksonXmlProperty(localName = "end-rule")
        private String endRule;
    }
}
