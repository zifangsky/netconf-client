package cn.zifangsky.netconf.huawei;

import cn.zifangsky.netconf.adapter.huawei.DefaultVsysEnums;
import cn.zifangsky.netconf.adapter.huawei.natStaticMapping.NatStaticMappingResolver;
import cn.zifangsky.netconf.adapter.huawei.natStaticMapping.model.*;
import cn.zifangsky.netconf.core.DefaultRpcManager;
import cn.zifangsky.netconf.core.Device;
import cn.zifangsky.netconf.core.exception.NetconfException;
import cn.zifangsky.netconf.adapter.huawei.natStaticMapping.NatStaticMappingResolverImpl;
import cn.zifangsky.netconf.adapter.huawei.natStaticMapping.model.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 测试--「华为防火墙-静态映射」
 *
 * @author zifangsky
 * @date 2021/2/18
 * @since 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("测试--「华为防火墙-静态映射」")
class NatStaticMappingResolverTest {
    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;
    
    private static NatStaticMappingResolver natStaticMappingResolver;

    @BeforeAll
    public static void init() throws NetconfException {
        Device device = Device.builder()
                .hostName(TEST_HOSTNAME)
                .userName(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .port(DEFAULT_NETCONF_PORT)
                .strictHostKeyChecking(false)
                .build();

        DefaultRpcManager rpcManager = new DefaultRpcManager(device);
        natStaticMappingResolver = new NatStaticMappingResolverImpl(rpcManager);
    }

    /**
     * 创建静态映射
     */
    @Test
    @Order(1)
    @DisplayName("创建静态映射")
    void createNatStaticMapping() throws IOException {
        //私网地址池
        InsidePool insidePool1 = new InsidePool(2, DefaultVsysEnums.PUBLIC.getCode(), new Section(1, "2.1.2.1", "2.1.2.5"));
        InsidePool insidePool2 = new InsidePool(3, DefaultVsysEnums.PUBLIC.getCode(), new Section(2, "2.1.3.1", "2.1.3.5"));
        List<InsidePool> insidePoolList = Arrays.asList(insidePool1, insidePool2);

        //公网地址池
        GlobalPool globalPool = new GlobalPool(3, DefaultVsysEnums.PUBLIC.getCode(), new Section(1, "5.9.8.1", "5.9.8.6"));

        //给接口「GigabitEthernet0/0/5」绑定静态映射
        StaticMapping staticMapping = new StaticMapping(1, DefaultVsysEnums.PUBLIC.getCode(),
                new MappingCfg(2, null, "GigabitEthernet0/0/5", 2050, 65532, 4095), new ExcludePort(5052, 5060));

        NatStaticMapping natStaticMapping = new NatStaticMapping(
                Arrays.asList(insidePool1, insidePool2),
                Collections.singletonList(globalPool),
                Collections.singletonList(staticMapping));

        boolean result = natStaticMappingResolver.createNatStaticMapping(natStaticMapping);
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

    /**
     * 修改静态映射
     */
    @Test
    @Order(2)
    @DisplayName("修改静态映射")
    void editNatStaticMapping() throws IOException {
        //修改静态映射ID为1的静态映射
        StaticMapping staticMapping = new StaticMapping(1, DefaultVsysEnums.PUBLIC.getCode(),
                new MappingCfg(3, null, "GigabitEthernet0/0/5", 30000, 40000, 4095), new ExcludePort(8000, 8100));

        NatStaticMapping natStaticMapping = new NatStaticMapping(Collections.singletonList(staticMapping));

        boolean result = natStaticMappingResolver.editNatStaticMapping(natStaticMapping);
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

    /**
     * 查看静态映射
     */
    @Test
    @Order(3)
    @DisplayName("查看静态映射")
    void getNatStaticMapping() throws IOException {
        NatStaticMapping natStaticMapping = new NatStaticMapping();

        NatStaticMapping result = natStaticMappingResolver.getNatStaticMapping(natStaticMapping);
        System.out.println("执行结果是：" + result);
        Assertions.assertNotNull(result);
    }

    /**
     * 删除静态映射
     */
    @Test
    @Order(4)
    @DisplayName("删除静态映射")
    void deleteNatStaticMapping() throws IOException {
        //删除ID为1的静态映射
        StaticMapping staticMapping = new StaticMapping(1, DefaultVsysEnums.PUBLIC.getCode());

        NatStaticMapping natStaticMapping = new NatStaticMapping(Collections.singletonList(staticMapping));

        boolean result = natStaticMappingResolver.deleteNatStaticMapping(natStaticMapping);
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }
}