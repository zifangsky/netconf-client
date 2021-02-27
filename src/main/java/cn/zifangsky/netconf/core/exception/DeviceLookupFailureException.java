package cn.zifangsky.netconf.core.exception;

import cn.zifangsky.netconf.core.Device;

/**
 * 查找{@link Device}失败异常
 *
 * @author zifangsky
 * @date 2021/2/27
 * @since 1.0.0
 */
public class DeviceLookupFailureException extends RuntimeException {

    public DeviceLookupFailureException(String msg) {
        super(msg);
    }

    public DeviceLookupFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
