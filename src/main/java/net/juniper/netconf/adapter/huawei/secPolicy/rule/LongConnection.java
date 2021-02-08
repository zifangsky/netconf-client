package net.juniper.netconf.adapter.huawei.secPolicy.rule;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略规则配置长连接，该节点仅用于容纳子节点，自身无数据意义。
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
public class LongConnection {
    /**
     * 表示安全策略规则配置基于策略的长连接功能。取值"true"，表示开启；取值为"false"，表示关闭；默认关闭。
     */
    @JacksonXmlProperty(localName = "enable-flag")
    private boolean enableFlag;

    /**
     * 表示安全策略规则配置长连接老化时间。整数形式，取值范围为0～24000，单位为小时。0表示不老化。缺省情况下，长连接的老化时间为168（7*24）小时。
     */
    @JacksonXmlProperty(localName = "during")
    private Integer during;
}
