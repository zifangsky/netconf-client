package cn.zifangsky.netconf.core.lookup;

import cn.zifangsky.netconf.core.Device;
import cn.zifangsky.netconf.core.exception.DeviceLookupFailureException;

/**
 * 自定义如何查找一个{@link Device}
 *
 * @author zifangsky
 * @date 2021/2/27
 * @since 1.0.0
 */
public interface DeviceLookup {
    /**
     * 通过给定的名称查找{@link Device}
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @param deviceName the given name
     * @return cn.zifangsky.netconf.core.Device
     * @throws DeviceLookupFailureException 查找{@link Device}失败异常
     */
    Device getDevice(String deviceName) throws DeviceLookupFailureException;
}
