package cn.zifangsky.netconf.adapter.huawei.rm.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * rmbase
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
public class RmBase {
    /**
     * uniAf
     */
    @JacksonXmlProperty(localName = "uniAf")
    @JacksonXmlElementWrapper(localName = "uniAfs")
    private List<UniAf> uniAf;

    public RmBase() {
    }

    public RmBase(List<UniAf> uniAf) {
        this.uniAf = uniAf;
    }
}
