package cn.zifangsky.netconf.adapter.huawei.save;

import java.io.IOException;

/**
 * 配置保存相关方法
 *
 * @author zifangsky
 * @date 2021/6/25
 * @since 1.0.0
 */
public interface SaveResolver {
    /**
     * 配置保存
     * @author zifangsky
     * @date 2021/6/25
     * @since 1.0.0
     * @param fileName 保存的配置文件名字
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean save(String fileName) throws IOException;
}