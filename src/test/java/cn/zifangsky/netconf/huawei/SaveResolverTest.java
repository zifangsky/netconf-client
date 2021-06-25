package cn.zifangsky.netconf.huawei;

import cn.zifangsky.netconf.adapter.huawei.save.SaveResolver;
import cn.zifangsky.netconf.adapter.huawei.save.SaveResolverImpl;
import cn.zifangsky.netconf.core.DefaultDevice;
import cn.zifangsky.netconf.core.Device;
import cn.zifangsky.netconf.core.RpcManager;
import cn.zifangsky.netconf.core.SingleDeviceRpcManager;
import cn.zifangsky.netconf.core.exception.NetconfException;
import org.junit.jupiter.api.*;

import java.io.IOException;

/**
 * 测试--「华为防火墙-配置保存」
 *
 * @author zifangsky
 * @date 2021/6/25
 * @since 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("测试--「华为防火墙-配置保存」")
class SaveResolverTest {
    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private static SaveResolver saveResolver;

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
        saveResolver = new SaveResolverImpl(rpcManager);
    }

    /**
     * 配置保存
     */
    @Test
    @Order(1)
    @DisplayName("配置保存")
    void save() throws IOException {
        boolean result = saveResolver.save("vrpcfg.zip");
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

}