package cn.zifangsky.netconf.adapter.huawei.securityZone.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * 安全区域
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "security-zone")
public class SecurityZone {
    /**
     * 安全区域实例
     */
    @JacksonXmlProperty(localName = "zone-instance")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ZoneInstance> zoneInstance;

    public SecurityZone() {
    }

    public SecurityZone(List<ZoneInstance> zoneInstance) {
        this.zoneInstance = zoneInstance;
    }
}
