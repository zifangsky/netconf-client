package net.juniper.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示一个私网地址池
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@Data
public class InsidePool {
    /**
     * 表示私网地址池ID，大小范围为1-200。
     */
    @JacksonXmlProperty(localName = "inside-id")
    private Integer insideId;

    /**
     * 表示一个虚拟系统，下发的私网地址池配置在该虚拟系统下，不同的虚拟系统下的配置是相互独立的。
     */
    @JacksonXmlProperty(localName = "vsys")
    private String vsys;

    /**
     * 表示在私网地址池中为静态映射配置IP地址段，仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "section")
    private Section section;

    public InsidePool() {
    }

    public InsidePool(Integer insideId, String vsys) {
        this.insideId = insideId;
        this.vsys = vsys;
    }

    public InsidePool(Integer insideId, String vsys, Section section) {
        this.insideId = insideId;
        this.vsys = vsys;
        this.section = section;
    }
}
