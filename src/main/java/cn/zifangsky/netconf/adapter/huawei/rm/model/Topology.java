package cn.zifangsky.netconf.adapter.huawei.rm.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * topology
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
public class Topology {
    /**
     * route
     */
    @JacksonXmlProperty(localName = "route")
    @JacksonXmlElementWrapper(localName = "routes")
    private List<Route> route;

    public Topology() {
    }

    public Topology(List<Route> route) {
        this.route = route;
    }
}
