package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示安全策略规则引用的应用类型，该节点仅用于容纳子节点，自身无数据意义。该节点的子节点为大类。
 *
 * @author zifangsky
 * @date 21/2/5
 * @since 1.0.0
 */
@Data
public class Category {
    /**
     * 表示安全策略规则引用的应用大类，该节点仅用于容纳子节点，自身无数据意义。该节点的子节点为大类名称及该大类的子类。
     */
    @JacksonXmlProperty(localName = "application-category")
    private ApplicationCategory applicationCategory;

    /* 其他内部属性 */
    /**
     * 安全策略规则引用的应用大类
     */
    @Data
    public static class ApplicationCategory {
        /**
         * 表示安全策略规则引用的应用大类，通过名称引用。
         */
        @JacksonXmlProperty(localName = "name")
        private String name;

        /**
         * 表示安全策略规则引用的应用小类，通过名称引用。
         */
        @JacksonXmlProperty(localName = "application-subcategory")
        private String applicationSubcategory;
    }
}
