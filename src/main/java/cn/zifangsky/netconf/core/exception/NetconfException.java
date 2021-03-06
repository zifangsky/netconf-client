/*
 Copyright (c) 2013 Juniper Networks, Inc.
 All Rights Reserved

 Use is subject to license terms.

*/

package cn.zifangsky.netconf.core.exception;

import java.io.IOException;

/**
 * Describes exceptions related to establishing Netconf session.
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.1.0
 */
public class NetconfException extends IOException {
    public NetconfException(String msg) {
        super(msg);
    }

    public NetconfException(String msg, Throwable t) {
        super(msg, t);
    }
}
