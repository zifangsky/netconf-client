package cn.zifangsky.netconf.core;

/**
 * 单实例{@link Device}实现的{@link RpcManager}
 *
 * @author zifangsky
 * @date 2021/2/27
 * @since 1.0.0
 */
public class SingleDeviceRpcManager extends AbstractExecutingRpcManager {

    public SingleDeviceRpcManager(Device device) {
        super(device);
    }

    /**
     * 单实例{@link Device}实现的{@link RpcManager}不再需要此方法
     * @author zifangsky
     * @date 2021/2/27
     * @since 1.0.0
     * @return java.lang.Object
     */
    @Override
    protected final Object determineCurrentLookupKey() {
        return null;
    }
}
