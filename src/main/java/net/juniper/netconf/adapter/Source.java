package net.juniper.netconf.adapter;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 用于指定不同的配置库
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.0.0
 */
@Data
public class Source {
    /**
     * 运行状态配置库（默认使用）
     */
    @JacksonXmlProperty(localName = "running")
    private Running running;

    public Source() {
    }

    public Source(Running running) {
        this.running = running;
    }

    /* 其他内部属性 */
    /**
     * 运行状态配置库（默认使用）
     */
    @Data
    public static class Running {
    }

}
