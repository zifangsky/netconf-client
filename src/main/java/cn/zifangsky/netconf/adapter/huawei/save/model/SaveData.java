package cn.zifangsky.netconf.adapter.huawei.save.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 配置保存的数据域
 *
 * @author zifangsky
 * @date 2021/6/25
 * @since 1.0.0
 */
@Data
public class SaveData {
    /**
     * response body
     */
    @JacksonXmlProperty(localName = "save")
    private Save save;
}
