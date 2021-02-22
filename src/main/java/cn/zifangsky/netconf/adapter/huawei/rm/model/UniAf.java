package cn.zifangsky.netconf.adapter.huawei.rm.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * uniAf
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
public class UniAf {
    /**
     * topology
     */
    @JacksonXmlProperty(localName = "topology")
    @JacksonXmlElementWrapper(localName = "topologys")
    private List<Topology> topology;

    public UniAf() {
    }

    public UniAf(List<Topology> topology) {
        this.topology = topology;
    }
}
