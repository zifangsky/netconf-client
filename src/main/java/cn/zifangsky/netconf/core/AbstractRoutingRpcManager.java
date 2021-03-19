package cn.zifangsky.netconf.core;

import cn.zifangsky.netconf.core.exception.NetconfException;
import cn.zifangsky.netconf.core.lookup.DeviceLookup;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于支持多 {@link Device} 实例动态切换
 *
 * @author zifangsky
 * @date 21/2/7
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractRoutingRpcManager implements RpcManager {
    /**
     * 自定义如何查找一个{@link Device}
     */
    private DeviceLookup deviceLookup;
    /**
     * 多个设备的连接实例
     */
    private Map<Object, Device> targetDevices;
    /**
     * 默认的设备连接实例
     */
    private Device defaultDevice;

    public AbstractRoutingRpcManager(Device defaultDevice) {
        this(new HashMap<>(), defaultDevice);
    }

    public AbstractRoutingRpcManager(Map<Object, Device> targetDevices, Device defaultDevice) {
        if(targetDevices == null){
            throw new IllegalArgumentException("Parameter 'targetDevices' cannot be empty.");
        }

        this.afterPropertiesSet(targetDevices, defaultDevice);
    }

    /**
     * 参数设置完成后进行初步的处理
     */
    public void afterPropertiesSet(Map<Object, Device> targetDevices, Device defaultDevice) {
        this.targetDevices = new HashMap<>(targetDevices.size());

        targetDevices.forEach((key, value) -> {
            Object lookupKey = this.resolveSpecifiedLookupKey(key);
            Device device = this.resolveSpecifiedDevice(value);

            this.targetDevices.put(lookupKey, device);
        });

        if(defaultDevice != null){
            this.defaultDevice = this.resolveSpecifiedDevice(defaultDevice);
        }
    }
    
    /**
     * 设置{@link DeviceLookup}
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @param deviceLookup deviceLookup
     */
    public void setDeviceLookup(DeviceLookup deviceLookup) {
        if(deviceLookup == null){
            throw new IllegalArgumentException("Parameter 'deviceLookup' cannot be empty.");
        }

        this.deviceLookup = deviceLookup;
    }

    /**
     * 抉择当前需要使用的{@link Device}
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @return cn.zifangsky.netconf.core.Device
     */
    protected synchronized Device determineTargetDevice() throws NetconfException {
        //1. 抉择当前需要使用的 lookup key
        Object lookupKey = this.determineCurrentLookupKey();

        //2. 获取实例
        Device device = null;
        if(lookupKey != null){
            device = this.targetDevices.get(lookupKey);
        }
        if(device == null){
            device = this.defaultDevice;
        }

        if(device == null){
            throw new IllegalStateException("Cannot determine target Device for lookup key [" + lookupKey + "]");
        }

        //3. 自动重连
        if(!device.isConnected()){
            device.connect();
        }

        return device;
    }

    /**
     * 抉择当前需要使用的{@link Device}对应的 lookup key
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @return java.lang.Object
     */
    protected abstract Object determineCurrentLookupKey();
    
    /**
     * 特殊情况下通过给出的 key object 获取{@link #targetDevices}需要的key
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @param lookupKey key object
     * @return java.lang.Object
     */
    protected Object resolveSpecifiedLookupKey(Object lookupKey) {
        return lookupKey;
    }

    /**
     * 特殊情况下通过给出的 device object 获取{@link Device}实例
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @param device device object
     * @return cn.zifangsky.netconf.core.Device
     */
    protected Device resolveSpecifiedDevice(Object device) throws IllegalArgumentException {
        if(device instanceof Device){
            return (Device) device;
        }else if(device instanceof String && (this.deviceLookup != null)){
            return this.deviceLookup.getDevice((String) device);
        }else {
            throw new IllegalArgumentException("Illegal data source value - only [cn.zifangsky.netconf.core.Device] and String supported: " + device);
        }
    }

    /**
     * 获取{@link #targetDevices}
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @return java.util.Map<java.lang.Object,cn.zifangsky.netconf.core.Device>
     */
    public Map<Object, Device> getTargetDevices() {
        return this.targetDevices;
    }

    /**
     * 获取{@link #defaultDevice}
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @return cn.zifangsky.netconf.core.Device
     */
    public Device getDefaultDevice() {
        return defaultDevice;
    }

    public void setTargetDevices(Map<Object, Device> targetDevices) {
        this.targetDevices = targetDevices;
    }

    public void setDefaultDevice(Device defaultDevice) {
        this.defaultDevice = defaultDevice;
    }
}
