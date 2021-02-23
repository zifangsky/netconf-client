package cn.zifangsky.netconf.adapter.huawei.securityZone.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 安全区域的数据域
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
public class SecurityZoneData {
    /**
     * response body
     */
    @JacksonXmlProperty(localName = "security-zone")
    private SecurityZone securityZone;
}
