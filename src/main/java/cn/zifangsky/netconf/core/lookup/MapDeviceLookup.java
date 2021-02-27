package cn.zifangsky.netconf.core.lookup;

import cn.zifangsky.netconf.core.Device;
import cn.zifangsky.netconf.core.exception.DeviceLookupFailureException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从多实例获取
 *
 * @author zifangsky
 * @date 2021/2/27
 * @since 1.0.0
 */
public class MapDeviceLookup implements DeviceLookup {

    private final Map<String, Device> devices = new ConcurrentHashMap<>(4);

    public MapDeviceLookup() {
    }

    public MapDeviceLookup(Map<String, Device> devices) {
        this.setDevices(devices);
    }

    @Override
    public Device getDevice(String deviceName) throws DeviceLookupFailureException {
        if(deviceName == null){
            throw new IllegalArgumentException("Parameter 'deviceName' cannot be empty.");
        }

        Device device = this.devices.get(deviceName);
        if(device == null){
            throw new DeviceLookupFailureException("No Device with name '" + deviceName + "' registered");
        }

        return device;
    }

    /**
     * 设置{@link Device}
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @param devices device Map
     */
    public void setDevices(Map<String, Device> devices){
        if(devices != null){
            this.devices.putAll(devices);
        }
    }

    /**
     * 添加{@link Device}
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @param deviceName 设备名称
     * @param device 设备
     */
    public void addDevice(String deviceName, Device device) {
        if(deviceName == null || device == null){
            throw new IllegalArgumentException("Parameter 'deviceName' or 'device' cannot be empty.");
        }

        this.devices.put(deviceName, device);
    }
}
