package cn.zifangsky.netconf.core.lookup;

import cn.zifangsky.netconf.core.Device;
import cn.zifangsky.netconf.core.exception.DeviceLookupFailureException;

/**
 * 从单实例获取
 *
 * @author zifangsky
 * @date 2021/2/27
 * @since 1.0.0
 */
public class SingleDeviceLookup implements DeviceLookup {

    private final Device device;

    public SingleDeviceLookup(Device device) {
        this.device = device;
    }

    @Override
    public Device getDevice(String deviceName) throws DeviceLookupFailureException {
        if(deviceName == null){
            throw new IllegalArgumentException("Parameter 'deviceName' cannot be empty.");
        }

        return this.device;
    }
}
