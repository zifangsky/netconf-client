package net.juniper.netconf.adapter.huawei.secPolicy;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import net.juniper.netconf.adapter.huawei.secPolicy.rule.Rule;

import java.util.List;

/**
 * 静态安全策略
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
public class StaticPolicy {
    /**
     * 表示安全策略的规则，仅用于容纳子节点，自身无数据意义。标签对<rule></rule>之间定义一条安全策略规则rule。安全策略规则可以包含源目安全域、源目地址、服务、应用、时间段等多种策略匹配条件，来对流量进行精细化匹配过滤。
     */
    @JacksonXmlProperty(localName = "rule")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Rule> rule;
}
