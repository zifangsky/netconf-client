package cn.zifangsky.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * 静态映射
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "nat-static-mapping")
public class NatStaticMapping {
    /**
     * 表示一个私网地址池，仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "inside-pool")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<InsidePool> insidePool;

    /**
     * 表示一个公网地址池，仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "global-pool")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GlobalPool> globalPool;

    /**
     * 表示配置静态映射关系，仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "static-mapping")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<StaticMapping> staticMapping;
}
