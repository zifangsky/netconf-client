package net.juniper.netconf.adapter.huawei.secPolicy;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 安全策略的数据域
 *
 * @author zifangsky
 * @date 2/4/21
 * @since 1.0.0
 */
@Data
public class SecPolicyData<T> {

    /**
     * response body
     */
    @JacksonXmlProperty(localName = "sec-policy", namespace = "urn:huawei:params:xml:ns:yang:huawei-security-policy")
    private T secPolicy;
}
