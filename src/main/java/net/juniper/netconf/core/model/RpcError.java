package net.juniper.netconf.core.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import net.juniper.netconf.adapter.huawei.ErrorTagEnums;

/**
 * rpc-error
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
public class RpcError {
    /**
     * 标识错误发生的层级
     */
    @JacksonXmlProperty(localName = "error-type")
    private String errorType;

    /**
     * 标识不同的错误类型
     */
    @JacksonXmlProperty(localName = "error-tag")
    private ErrorTagEnums errorTag;

    /**
     * 标识错误的严重级别
     */
    @JacksonXmlProperty(localName = "error-severity")
    private String errorSeverity;

    /**
     * 标识出错的节点
     */
    @JacksonXmlProperty(localName = "error-path")
    private String errorPath;

    /**
     * 描述错误信息，可选元素
     */
    @JacksonXmlProperty(localName = "error-message")
    private String errorMessage;
}
