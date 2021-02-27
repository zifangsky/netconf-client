package cn.zifangsky.netconf.huawei;

import cn.zifangsky.netconf.adapter.huawei.DefaultVsysEnums;
import cn.zifangsky.netconf.adapter.huawei.securityZone.SecurityZoneResolver;
import cn.zifangsky.netconf.adapter.huawei.securityZone.SecurityZoneResolverImpl;
import cn.zifangsky.netconf.adapter.huawei.securityZone.model.SecurityZone;
import cn.zifangsky.netconf.adapter.huawei.securityZone.model.ZoneInstance;
import cn.zifangsky.netconf.core.DefaultDevice;
import cn.zifangsky.netconf.core.Device;
import cn.zifangsky.netconf.core.RpcManager;
import cn.zifangsky.netconf.core.SingleDeviceRpcManager;
import cn.zifangsky.netconf.core.exception.NetconfException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 测试--「华为防火墙-安全区域」
 *
 * @author zifangsky
 * @date 2021/2/23
 * @since 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("测试--「华为防火墙-安全区域」")
class SecurityZoneResolverTest {
    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private static SecurityZoneResolver securityZoneResolver;

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
        securityZoneResolver = new SecurityZoneResolverImpl(rpcManager);
    }

    /**
     * 创建安全区域
     */
    @Test
    @Order(1)
    @DisplayName("创建安全区域")
    void createSecurityZone() throws IOException {
        ZoneInstance zoneInstance = new ZoneInstance("test_by_code_zone", DefaultVsysEnums.PUBLIC.getCode(),
                "通过程序自动创建的安全区域", 66, "WAN0/0/1");

        boolean result = securityZoneResolver.createSecurityZone(Collections.singletonList(zoneInstance));
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);

    }

    /**
     * 修改安全区域
     */
    @Test
    @Order(2)
    @DisplayName("修改安全区域")
    void editSecurityZone() throws IOException {
        //修改名称为「test_by_code_zone」的安全区域
        ZoneInstance zoneInstance = new ZoneInstance("test_by_code_zone", DefaultVsysEnums.PUBLIC.getCode(),
                "通过程序自动创建的安全区域（已修改）", 67, "WAN0/0/1");

        boolean result = securityZoneResolver.editSecurityZone(Collections.singletonList(zoneInstance));
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

    /**
     * 查看安全区域
     */
    @Test
    @Order(3)
    @DisplayName("查看安全区域")
    void getSecurityZone() throws IOException {
        List<ZoneInstance> result = securityZoneResolver.getSecurityZone(new SecurityZone());
        System.out.println("执行结果是：" + result);
        Assertions.assertNotNull(result);
    }

    /**
     * 删除安全区域
     */
    @Test
    @Order(4)
    @DisplayName("删除安全区域")
    void deleteSecurityZone() throws IOException {
        //删除名称为「test_by_code_zone」的安全区域
        ZoneInstance zoneInstance = new ZoneInstance("test_by_code_zone", DefaultVsysEnums.PUBLIC.getCode());

        boolean result = securityZoneResolver.deleteSecurityZone(Collections.singletonList(zoneInstance));
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }
}