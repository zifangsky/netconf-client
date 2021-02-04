package net.juniper.netconf.adapter.huawei.secPolicy;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 表示请求操作（创建、删除、修改、查询）的对象是安全策略
 *
 * @author zifangsky
 * @date 2/4/21
 * @since 1.0.0
 */
@Data
public class SecPolicy {

    /**
     * 表示一个虚拟系统，下发的安全策略在该虚拟系统下配置，不同的虚拟系统下的配置是相互独立的。标签对<vsys></vsys>之间表示一个虚拟系统。
     */
    @JacksonXmlProperty(localName = "vsys")
    private String virtualSystem;
}
