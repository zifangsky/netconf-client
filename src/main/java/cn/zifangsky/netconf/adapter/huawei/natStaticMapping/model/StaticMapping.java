package cn.zifangsky.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示配置静态映射关系
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@Data
public class StaticMapping {
    /**
     * 表示静态映射ID，大小范围为1-200。
     */
    @JacksonXmlProperty(localName = "static-mapping-id")
    private Integer staticMappingId;

    /**
     * 表示一个虚拟系统，下发的静态映射配置在该虚拟系统下，不同的虚拟系统下的配置是相互独立的。
     */
    @JacksonXmlProperty(localName = "vsys")
    private String vsys;

    /**
     * 表示配置以section为单位计算公私网映射关系，仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "mapping-cfg")
    private MappingCfg mappingCfg;

    /**
     * 表示用来排除参与静态映射的公网端口段。
     */
    @JacksonXmlProperty(localName = "exclude-port")
    private ExcludePort excludePort;

    public StaticMapping() {
    }

    public StaticMapping(Integer staticMappingId, String vsys) {
        this(staticMappingId, vsys, null, null);
    }

    public StaticMapping(Integer staticMappingId, String vsys, MappingCfg mappingCfg, ExcludePort excludePort) {
        this.staticMappingId = staticMappingId;
        this.vsys = vsys;
        this.mappingCfg = mappingCfg;
        this.excludePort = excludePort;
    }
}
