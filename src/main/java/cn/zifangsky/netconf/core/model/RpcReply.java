package cn.zifangsky.netconf.core.model;

import cn.zifangsky.netconf.core.NetconfConstants;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * rpc-reply
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "rpc-reply", namespace = NetconfConstants.URN_XML_NS_NETCONF_BASE_1_0)
public class RpcReply<T> {
    /**
     * message-id
     */
    @JacksonXmlProperty(isAttribute = true, localName = "message-id")
    private String messageId;

    /**
     * response body
     */
    @JacksonXmlProperty(localName = "data")
    private T data;

    /**
     * 执行成功标识
     */
    @JacksonXmlProperty(localName = "ok")
    private String ok;

    /**
     * 执行失败标识
     */
    @JacksonXmlProperty(localName = "rpc-error")
    private RpcError error;
}
