package net.juniper.netconf.adapter.huawei;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import net.juniper.netconf.adapter.IFilter;
import net.juniper.netconf.adapter.huawei.secPolicy.SecPolicy;

/**
 * 过滤信息
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "filter")
public class Filter implements IFilter {
    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private String type = "subtree";

    /**
     * request body
     */
    @JacksonXmlProperty(localName = "sec-policy")
    private SecPolicy secPolicy;
}
