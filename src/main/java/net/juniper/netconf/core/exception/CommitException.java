/*
 Copyright (c) 2013 Juniper Networks, Inc.
 All Rights Reserved

 Use is subject to license terms.

*/

package net.juniper.netconf.core.exception;

import java.io.IOException;

/**
 * Describes exceptions related to commit operation
 *
 * @author zifangsky
 * @date 2021/2/8
 * @since 1.1.0
 */
public class CommitException extends IOException {
    public CommitException(String msg) {
        super(msg);
    }
}
