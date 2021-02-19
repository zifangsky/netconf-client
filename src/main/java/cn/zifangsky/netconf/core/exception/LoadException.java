/*
 Copyright (c) 2013 Juniper Networks, Inc.
 All Rights Reserved

 Use is subject to license terms.

*/

package cn.zifangsky.netconf.core.exception;

import java.io.IOException;

/** 
 * Describes exceptions related to load operation
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.1.0
 */
public class LoadException extends IOException {

    public LoadException(String msg) {
        super(msg);
    }
}
