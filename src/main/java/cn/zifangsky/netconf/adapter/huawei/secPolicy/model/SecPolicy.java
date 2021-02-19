package cn.zifangsky.netconf.adapter.huawei.secPolicy.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * 表示请求操作（创建、删除、修改、查询）的对象是安全策略
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "sec-policy")
public class SecPolicy {
    /**
     * 表示一个虚拟系统，下发的安全策略在该虚拟系统下配置，不同的虚拟系统下的配置是相互独立的。标签对<vsys></vsys>之间表示一个虚拟系统。
     */
    @JacksonXmlProperty(localName = "vsys")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<VirtualSystem> vsys;
}
