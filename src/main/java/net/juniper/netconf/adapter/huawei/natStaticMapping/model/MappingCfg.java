package net.juniper.netconf.adapter.huawei.natStaticMapping.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示配置以section为单位计算公私网映射关系
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@Data
public class MappingCfg {
    /**
     * 表示静态映射下私网地址池ID，大小范围为1-200。
     */
    @JacksonXmlProperty(localName = "inside-pool-id")
    private Integer insidePoolId;

    /**
     * 表示静态映射下公网地址池ID，大小范围为1-200。
     */
    @JacksonXmlProperty(localName = "global-pool-id")
    private Integer globalPoolId;

    /**
     * 表示静态映射下的接口名字，有物理接口、隧道、vlanif
     */
    @JacksonXmlProperty(localName = "interface-name")
    private String interfaceName;

    /**
     * 表示静态映射下的起始端口，整数形式，取值范围为256～65535。
     */
    @JacksonXmlProperty(localName = "start-port")
    private Integer startPort;

    /**
     * 表示静态映射下的结束端口，整数形式，取值范围为256～65535。
     */
    @JacksonXmlProperty(localName = "end-port")
    private Integer endPort;

    /**
     * 表示静态映射下端口块大小取值范围。
     */
    @JacksonXmlProperty(localName = "port-size")
    private Integer portSize;

    /**
     * 表示以section为单位计算公私网映射关系。
     */
    @JacksonXmlProperty(localName = "in-section")
    private Boolean inSection;

    /**
     * 表示以地址池为单位计算公私网映射关系时，优先选择地址池中公网地址对私网用户进行转换，如果地址池中地址转换完再转换端口。
     */
    @JacksonXmlProperty(localName = "ip-first")
    private Boolean ipFirst;

    public MappingCfg() {
    }

    public MappingCfg(Integer insidePoolId, Integer globalPoolId, String interfaceName) {
        this(insidePoolId, globalPoolId, interfaceName, null, null, null);
    }

    public MappingCfg(Integer insidePoolId, Integer globalPoolId, String interfaceName, Integer startPort, Integer endPort, Integer portSize) {
        this.insidePoolId = insidePoolId;
        this.globalPoolId = globalPoolId;
        this.interfaceName = interfaceName;
        this.startPort = startPort;
        this.endPort = endPort;
        this.portSize = portSize;
    }
}
