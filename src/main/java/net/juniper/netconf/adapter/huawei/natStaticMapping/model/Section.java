package net.juniper.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示在 私网地址池/公网地址池 中为静态映射配置IP地址段
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@Data
public class Section {
    /**
     * 表示私网地址池地址段ID，大小范围为1-200。
     */
    @JacksonXmlProperty(localName = "section-id")
    private Integer sectionId;

    /**
     * 表示私网地址池地址段的起始ipv4地址。点分十进制格式，不需要掩码，如192.168.1.1。与下文<end-ip>标签必须成对出现。
     */
    @JacksonXmlProperty(localName = "start-ip")
    private String startIp;

    /**
     * 表示私网地址池地址段的结束ipv4地址。点分十进制格式，不需要掩码，如192.168.1.1。与上文<start-ip>标签必须成对出现。
     */
    @JacksonXmlProperty(localName = "end-ip")
    private String endIp;

    public Section() {
    }

    public Section(Integer sectionId, String startIp, String endIp) {
        this.sectionId = sectionId;
        this.startIp = startIp;
        this.endIp = endIp;
    }
}
