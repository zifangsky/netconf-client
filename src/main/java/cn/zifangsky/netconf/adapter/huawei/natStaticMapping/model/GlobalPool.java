package cn.zifangsky.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示一个公网地址池
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@Data
public class GlobalPool {
    /**
     * 表示公网地址池ID，大小范围为1-200。
     */
    @JacksonXmlProperty(localName = "global-id")
    private Integer globalId;

    /**
     * 表示一个虚拟系统，下发的公网地址池配置在该虚拟系统下，不同的虚拟系统下的配置是相互独立的。
     */
    @JacksonXmlProperty(localName = "vsys")
    private String vsys;

    /**
     * 表示地址转换模式，只有full-cone一个选项。
     */
    @JacksonXmlProperty(localName = "mode")
    private ModeEnums mode;

    /**
     * 表示在公网地址池中为静态映射配置IP地址段，仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "section")
    private Section section;

    /**
     * 表示是否开启静态映射公网地址池中全局地址池统计功能。
     */
    @JacksonXmlProperty(localName = "statistics-enable")
    private Boolean statisticsEnable;

    /**
     * 表示是否开启静态映射公网地址池中地址的UNR路由下发功能。
     */
    @JacksonXmlProperty(localName = "route-enable")
    private Boolean routeEnable;

    public GlobalPool() {
    }

    public GlobalPool(Integer globalId, String vsys) {
        this.globalId = globalId;
        this.vsys = vsys;
    }

    public GlobalPool(Integer globalId, String vsys, Section section) {
        this.globalId = globalId;
        this.vsys = vsys;
        this.section = section;
        this.mode = ModeEnums.FULL_CONE;
    }
}
