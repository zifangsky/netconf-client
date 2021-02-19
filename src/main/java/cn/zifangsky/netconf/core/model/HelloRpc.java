package cn.zifangsky.netconf.core.model;

import cn.zifangsky.netconf.core.NetconfConstants;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * hello报文
 *
 * @author zifangsky
 * @date 21/2/8
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "hello", namespace = NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0)
public class HelloRpc {
    /**
     * capability
     */
    @JacksonXmlProperty(localName = "capability")
    @JacksonXmlElementWrapper(localName = "capabilities")
    private List<String> capabilities;

    /**
     * sessionId
     */
    @JacksonXmlProperty(localName = "session-id")
    private String sessionId;
}
