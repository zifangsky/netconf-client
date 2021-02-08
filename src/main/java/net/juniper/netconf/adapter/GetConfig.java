package net.juniper.netconf.adapter;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * <get-config>
 * <p>获取数据，只包含配置数据，不包含运行状态数据。</p>
 * <p><filter>元素：用于过滤信息，没有指定<filter>元素则返回整个配置数据集的信息。</p>
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "get-config")
public class GetConfig<T extends IFilter> {
    /**
     * 用于指定不同的配置库
     */
    @JacksonXmlProperty(localName = "source")
    private Source source;

    /**
     * 适配不同厂商的过滤信息
     */
    @JacksonXmlProperty(localName = "filter")
    private T filter;

    public GetConfig() {
    }

    public GetConfig(Source source, T filter) {
        this.source = source;
        this.filter = filter;
    }
}
