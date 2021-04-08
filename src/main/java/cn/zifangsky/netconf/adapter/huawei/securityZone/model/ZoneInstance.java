package cn.zifangsky.netconf.adapter.huawei.securityZone.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 安全区域实例
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
public class ZoneInstance {
    /**
     * 字符串形式，表示安全域的的名字，长度为1~32字节，支持空格。
     */
    @JacksonXmlProperty(localName = "name")
    private String name;

    /**
     * 表示该安全域属于的虚系统，如果属于根系统，则使用public，具体约束和虚系统名称约束相同。
     */
    @JacksonXmlProperty(localName = "vsys")
    private String vsys;

    /**
     * 表示安全域的描述信息，字符串格式。
     */
    @JacksonXmlProperty(localName = "desc")
    private String desc;

    /**
     * 表示安全域的优先级，范围为1-100。
     */
    @JacksonXmlProperty(localName = "priority")
    private Integer priority;

    /**
     * 表示加入安全域的接口。
     */
    @JacksonXmlProperty(localName = "assign-interface")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> assignInterface;

    public ZoneInstance() {
    }

    public ZoneInstance(String name, String vsys) {
        this.name = name;
        this.vsys = vsys;
    }

    public ZoneInstance(String name, String vsys, String desc, Integer priority, String assignInterface) {
        this.name = name;
        this.vsys = vsys;
        this.desc = desc;
        this.priority = priority;
        this.assignInterface = Collections.singletonList(assignInterface);
    }

    public ZoneInstance(String name, String vsys, String desc, Integer priority, List<String> assignInterface) {
        this.name = name;
        this.vsys = vsys;
        this.desc = desc;
        this.priority = priority;
        this.assignInterface = assignInterface;
    }
}
