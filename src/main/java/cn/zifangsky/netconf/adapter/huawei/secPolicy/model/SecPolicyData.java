package cn.zifangsky.netconf.adapter.huawei.secPolicy.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 安全策略的数据域
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
public class SecPolicyData {
    /**
     * response body
     */
    @JacksonXmlProperty(localName = "sec-policy")
    private SecPolicy secPolicy;
}
