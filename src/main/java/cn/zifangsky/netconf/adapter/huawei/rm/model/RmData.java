package cn.zifangsky.netconf.adapter.huawei.rm.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * rm的数据域
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@Data
public class RmData {
    /**
     * response body
     */
    @JacksonXmlProperty(localName = "rm")
    private Rm rm;
}
