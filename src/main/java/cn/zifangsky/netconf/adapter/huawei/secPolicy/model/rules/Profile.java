package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略规则引用安全配置文件，该节点仅用于容纳子节点，自身无数据意义。安全策略规则引用安全配置文件的作用是对匹配安全策略规则的流量进行内容安全检测。
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
public class Profile {
    /**
     * 表示安全策略规则引用反病毒配置文件，通过名称引用。
     */
    @JacksonXmlProperty(localName = "av-profile")
    private String avProfile;

    /**
     * 表示安全策略规则引用入侵防御配置文件，通过名称引用。
     */
    @JacksonXmlProperty(localName = "ips-profile")
    private String ipsProfile;

    /**
     * 表示安全策略规则引用URL过滤配置文件，通过名称引用。
     */
    @JacksonXmlProperty(localName = "url-profile")
    private String urlProfile;

    /**
     * 表示安全策略规则引用文件过滤配置文件，通过名称引用。
     */
    @JacksonXmlProperty(localName = "fileblock-profile")
    private String fileBlockProfile;

    /**
     * 表示安全策略规则引用内容过滤配置文件，通过名称引用。
     */
    @JacksonXmlProperty(localName = "datafilter-profile")
    private String dataFilterProfile;

    /**
     * 表示安全策略规则引用应用行为控制配置文件，通过名称引用。
     */
    @JacksonXmlProperty(localName = "appctrl-profile")
    private String appCtrlProfile;

    /**
     * 表示安全策略规则引用邮件过滤配置文件，通过名称引用。
     */
    @JacksonXmlProperty(localName = "mailfilter-profile")
    private String mailFilterProfile;
}
