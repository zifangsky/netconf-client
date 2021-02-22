package cn.zifangsky.netconf.adapter.huawei.rm.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * rm
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "rm")
public class Rm {
    /**
     * rmbase
     */
    @JacksonXmlProperty(localName = "rmbase")
    private RmBase rmBase;

    public Rm() {
    }

    public Rm(RmBase rmBase) {
        this.rmBase = rmBase;
    }
}
