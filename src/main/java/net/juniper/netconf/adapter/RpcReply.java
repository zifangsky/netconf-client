package net.juniper.netconf.adapter;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * rpc-reply
 *
 * @author zifangsky
 * @date 2/4/21
 * @since 1.0.0
 */
@Data
@JacksonXmlRootElement(localName = "rpc-reply", namespace = "urn:ietf:params:xml:ns:netconf:base:1.0")
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
}
