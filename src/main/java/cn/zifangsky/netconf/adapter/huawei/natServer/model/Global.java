package cn.zifangsky.netconf.adapter.huawei.natServer.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示NAT Server对外提供的地址实例，可以是某个接口的IP或者配置的IP地址段
 *
 * @author zifangsky
 * @date 2021/2/19
 * @since 1.0.0
 */
@Data
public class Global {
    /**
     * 表示NAT Server对外提供的公网IP地址是接口的IP，如：GigabitEthernet1/0/0
     */
    @JacksonXmlProperty(localName = "if-type")
    private String ifType;

    /**
     * 表示公网IP地址段的起始地址，为必选项。
     */
    @JacksonXmlProperty(localName = "start-ip")
    private String startIp;

    /**
     * 表示公网IP地址段的结束地址，为可选项。
     */
    @JacksonXmlProperty(localName = "end-ip")
    private String endIp;

    public Global() {
    }

    public Global(String ifType) {
        this.ifType = ifType;
    }

    public Global(String startIp, String endIp) {
        this.startIp = startIp;
        this.endIp = endIp;
    }
}
