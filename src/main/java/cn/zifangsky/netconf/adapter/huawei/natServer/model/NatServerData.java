package cn.zifangsky.netconf.adapter.huawei.natServer.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * NAT Server（服务器映射）的数据域
 *
 * @author zifangsky
 * @date 2021/2/19
 * @since 1.0.0
 */
@Data
public class NatServerData {
    /**
     * response body
     */
    @JacksonXmlProperty(localName = "nat-server")
    private NatServer natServer;
}
