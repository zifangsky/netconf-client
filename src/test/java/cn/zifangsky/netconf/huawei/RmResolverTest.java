package cn.zifangsky.netconf.huawei;

import cn.zifangsky.netconf.adapter.huawei.rm.RmResolver;
import cn.zifangsky.netconf.adapter.huawei.rm.RmResolverImpl;
import cn.zifangsky.netconf.adapter.huawei.rm.model.Rm;
import cn.zifangsky.netconf.adapter.huawei.rm.model.Route;
import cn.zifangsky.netconf.core.DefaultDevice;
import cn.zifangsky.netconf.core.Device;
import cn.zifangsky.netconf.core.RpcManager;
import cn.zifangsky.netconf.core.SingleDeviceRpcManager;
import cn.zifangsky.netconf.core.exception.NetconfException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

/**
 * 测试--「华为防火墙-路由状态」
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("测试--「华为防火墙-路由状态」")
class RmResolverTest {
    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private static RmResolver rmResolver;

    @BeforeAll
    public static void init() throws NetconfException {
        Device device = DefaultDevice.builder()
                .hostName(TEST_HOSTNAME)
                .userName(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .port(DEFAULT_NETCONF_PORT)
                .strictHostKeyChecking(false)
                .build();

        RpcManager rpcManager = new SingleDeviceRpcManager(device);
        rmResolver = new RmResolverImpl(rpcManager);
    }

    /**
     * 查看路由状态
     */
    @Test
    @Order(1)
    @DisplayName("查看路由状态")
    void getNatServerMapping() throws IOException {
        List<Route> result = rmResolver.getRouteState(new Rm());
        System.out.println("执行结果是：" + result);
        Assertions.assertNotNull(result);
    }
}