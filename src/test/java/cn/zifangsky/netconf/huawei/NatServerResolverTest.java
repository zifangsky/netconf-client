package cn.zifangsky.netconf.huawei;

import cn.zifangsky.netconf.adapter.huawei.DefaultVsysEnums;
import cn.zifangsky.netconf.adapter.huawei.natServer.NatServerResolver;
import cn.zifangsky.netconf.adapter.huawei.natServer.NatServerResolverImpl;
import cn.zifangsky.netconf.adapter.huawei.natServer.model.*;
import cn.zifangsky.netconf.core.*;
import cn.zifangsky.netconf.core.enums.ProtocolEnums;
import cn.zifangsky.netconf.core.exception.NetconfException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 测试--「华为防火墙-NAT Server（服务器映射）」
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("测试--「华为防火墙-NAT Server（服务器映射）」")
class NatServerResolverTest {
    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private static NatServerResolver natServerResolver;

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
        natServerResolver = new NatServerResolverImpl(rpcManager);
    }

    /**
     * 创建NAT Server
     */
    @Test
    @Order(1)
    @DisplayName("创建NAT Server")
    void createNatServerMapping() throws IOException {
        ServerMapping newMapping = new ServerMapping("test_by_code_nat", DefaultVsysEnums.PUBLIC.getCode(), ProtocolEnums.TCP,
                new Global("172.24.31.180", null), new Port(8080), new Inside("192.168.2.100", null), new Port(18080));

        boolean result = natServerResolver.createNatServerMapping(Collections.singletonList(newMapping));
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

    /**
     * 修改NAT Server
     */
    @Test
    @Order(2)
    @DisplayName("修改NAT Server")
    void editNatServerMapping() throws IOException {
        //修改名称为「test_by_code」的服务器映射
        ServerMapping mapping = new ServerMapping("test_by_code_nat", DefaultVsysEnums.PUBLIC.getCode(), ProtocolEnums.TCP,
                new Global("172.24.31.180", null), new Port(8080), new Inside("192.168.2.100", null), new Port(18081));

        boolean result = natServerResolver.editNatServerMapping(Collections.singletonList(mapping));
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

    /**
     * 查看NAT Server
     */
    @Test
    @Order(3)
    @DisplayName("查看NAT Server")
    void getNatServerMapping() throws IOException {
        List<ServerMapping> result = natServerResolver.getNatServerMapping(new NatServer());
        System.out.println("执行结果是：" + result);
        Assertions.assertNotNull(result);
    }

    /**
     * 删除NAT Server
     */
    @Test
    @Order(4)
    @DisplayName("删除NAT Server")
    void deleteNatServerMapping() throws IOException {
        //删除名称为「test_by_code」的服务器映射
        ServerMapping deleteMapping = new ServerMapping("test_by_code_nat", DefaultVsysEnums.PUBLIC.getCode());

        boolean result = natServerResolver.deleteNatServerMapping(Collections.singletonList(deleteMapping));
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }
}