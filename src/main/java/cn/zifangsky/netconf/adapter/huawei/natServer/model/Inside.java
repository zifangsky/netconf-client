package cn.zifangsky.netconf.adapter.huawei.natServer.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示NAT Server服务器在内部局域网的IP地址实例
 *
 * @author zifangsky
 * @date 2021/2/19
 * @since 1.0.0
 */
@Data
public class Inside {
    /**
     * 表示内部IP地址段的起始地址，为必选项。
     */
    @JacksonXmlProperty(localName = "start-ip")
    private String startIp;

    /**
     * 表示内部IP地址段的结束地址，为可选项。
     */
    @JacksonXmlProperty(localName = "end-ip")
    private String endIp;

    public Inside() {
    }

    public Inside(String startIp, String endIp) {
        this.startIp = startIp;
        this.endIp = endIp;
    }
}
