package cn.zifangsky.netconf.adapter.huawei.save.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置保存
 *
 * @author zifangsky
 * @date 2021/6/25
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "save")
public class Save {
    /**
     * 占位符
     */
    @JacksonXmlProperty(localName = "save")
    private String save = "";

    /**
     * 配置文件名字，比如：当前配置保存到名字为vsys1.cfg的配置文件中
     */
    @JacksonXmlProperty(localName = "file-name")
    private String fileName;

    public Save(String fileName) {
        this.fileName = fileName;
    }
}
