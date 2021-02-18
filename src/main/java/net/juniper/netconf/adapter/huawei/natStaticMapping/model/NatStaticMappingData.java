package net.juniper.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 静态映射的数据域
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@Data
public class NatStaticMappingData {
    /**
     * response body
     */
    @JacksonXmlProperty(localName = "nat-static-mapping")
    private NatStaticMapping natStaticMapping;
}
