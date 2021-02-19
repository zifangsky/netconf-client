package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * <p>表示安全策略规则引用应用信息，该节点仅用于容纳子节点，自身无数据意义，一条安全规则中最多只能出现1次，可选项。</p>
 * <p>该节点的子节点可以是应用、应用组、应用大类、应用小类。</p>
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
public class Application {
    /**
     * 表示安全策略规则引用的应用，通过名称引用。
     */
    @JacksonXmlProperty(localName = "application-object")
    private String applicationObject;

    /**
     * 表示安全策略规则引用的应用组，通过名称引用。
     */
    @JacksonXmlProperty(localName = "application-group")
    private String applicationGroup;

    /**
     * 表示安全策略规则引用的应用标签，通过名称引用。
     */
    @JacksonXmlProperty(localName = "application-label")
    private String applicationLabel;

    /**
     * 表示安全策略规则引用的软件，通过名称引用。
     */
    @JacksonXmlProperty(localName = "application-software")
    private String applicationSoftware;

    /**
     * 表示安全策略规则引用的应用类型，该节点仅用于容纳子节点，自身无数据意义。该节点的子节点为大类。
     */
    @JacksonXmlProperty(localName = "category")
    private Category category;
}
