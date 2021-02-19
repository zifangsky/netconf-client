package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略引用用户配置，该节点仅用于容纳节点，自身无数据意义
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
public class SourceUser {
    /**
     * 表示安全策略规则引用的用户，通过名称引用
     */
    @JacksonXmlProperty(localName = "users")
    private String users;

    /**
     * 表示安全策略规则引用的用户组，通过名称引用
     */
    @JacksonXmlProperty(localName = "user-group")
    private String userGroup;

    /**
     * 表示安全策略规则引用的安全组，通过名称引用
     */
    @JacksonXmlProperty(localName = "security-group")
    private String securityGroup;
}
