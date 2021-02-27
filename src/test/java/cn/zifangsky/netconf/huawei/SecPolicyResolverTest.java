package cn.zifangsky.netconf.huawei;

import cn.zifangsky.netconf.adapter.huawei.ActionEnums;
import cn.zifangsky.netconf.adapter.huawei.DefaultVsysEnums;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.SecPolicyResolver;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.SecPolicyResolverImpl;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.SecPolicy;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.StaticPolicy;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.VirtualSystem;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules.*;
import cn.zifangsky.netconf.core.DefaultDevice;
import cn.zifangsky.netconf.core.Device;
import cn.zifangsky.netconf.core.RpcManager;
import cn.zifangsky.netconf.core.SingleDeviceRpcManager;
import cn.zifangsky.netconf.core.exception.NetconfException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * 测试--「华为防火墙-安全策略」
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("测试--「华为防火墙-安全策略」")
class SecPolicyResolverTest {
    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private static SecPolicyResolver secPolicyResolver;

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
        secPolicyResolver = new SecPolicyResolverImpl(rpcManager);
    }


    /**
     * 创建安全策略
     */
    @Test
    @Order(1)
    @DisplayName("创建安全策略")
    void createSecPolicy() throws IOException {
        //只设置目的端口
        ServiceItems item = new ServiceItems(new Tcp("700 888 to 999 1023"));
//        ServiceItems item = new ServiceItems(new Tcp("100 200 to 300 600", "700 888 to 999 1023"));
        //如果希望端口设置为 any，则 new Service() 即可；如果希望不设置安全区域，则将对应字段设置为空即可
//        Rule newRule = new Rule("test_by_code_sec_policy", "通过程序自动下发配置", ActionEnums.TRUE, null, null,
//                new Address("1.1.1.1/32", true), new Address("1.1.1.2/32", true), new Service());
        //也可以将服务设置成预设的某些服务
//        Rule newRule = new Rule("test_by_code_sec_policy", "通过程序自动下发配置", ActionEnums.TRUE, null, null,
//                new Address("1.1.1.1/32", true), new Address("1.1.1.2/32", true), new Service(Arrays.asList("smtp", "smtps")));

        //一条新策略
        Rule newRule = new Rule("test_by_code_sec_policy", "通过程序自动下发配置", ActionEnums.TRUE, "untrust", "trust",
                new Address(Arrays.asList("1.1.1.1", "10.1.1.0/24"), true), new Address(Collections.singletonList("1.1.1.2/32"), true), new Service(item));
        newRule.setParentGroup("策略组B");

        StaticPolicy staticPolicy = new StaticPolicy(Collections.singletonList(newRule));
        VirtualSystem vsy = new VirtualSystem(DefaultVsysEnums.PUBLIC.getCode(), staticPolicy);
        SecPolicy secPolicy = new SecPolicy(Collections.singletonList(vsy));

        boolean result = secPolicyResolver.createSecPolicy(secPolicy);
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

    /**
     * 修改安全策略
     */
    @Test
    @Order(2)
    @DisplayName("修改安全策略")
    void editSecPolicy() throws IOException {
        //已有的一条策略
        ServiceItems item = new ServiceItems(new Tcp("100 200 to 300 600", "700 888 to 999 1023 to 1030"));
        //保持名称不变
        Rule rule = new Rule("test_by_code_sec_policy", "通过程序自动修改配置", ActionEnums.FALSE, "untrust", "trust",
                new Address(Arrays.asList("1.1.1.1/32", "10.1.1.0/24"), true), new Address(Collections.singletonList("1.1.1.2/32"), true), new Service(item));

        StaticPolicy staticPolicy = new StaticPolicy(Collections.singletonList(rule));
        VirtualSystem vsy = new VirtualSystem(DefaultVsysEnums.PUBLIC.getCode(), staticPolicy);
        SecPolicy secPolicy = new SecPolicy(Collections.singletonList(vsy));

        boolean result = secPolicyResolver.editSecPolicy(secPolicy);
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

    /**
     * 查看安全策略
     */
    @Test
    @Order(3)
    @DisplayName("查看安全策略")
    void getSecPolicy() throws IOException {
        //只查询「静态安全策略」
        VirtualSystem vsy = new VirtualSystem(DefaultVsysEnums.PUBLIC.getCode(), new StaticPolicy());
        SecPolicy secPolicy = new SecPolicy(Collections.singletonList(vsy));

        SecPolicy result = secPolicyResolver.getSecPolicy(secPolicy);
        System.out.println("执行结果是：" + result);
        Assertions.assertNotNull(result);
    }

    /**
     * 删除安全策略
     */
    @Test
    @Order(4)
    @DisplayName("删除安全策略")
    void deleteSecPolicy() throws IOException {
        //提供需要删除策略的名称
        Rule deleteRule = new Rule("test_by_code_sec_policy");

        StaticPolicy staticPolicy = new StaticPolicy(Collections.singletonList(deleteRule));
        VirtualSystem vsy = new VirtualSystem(DefaultVsysEnums.PUBLIC.getCode(), staticPolicy);
        SecPolicy secPolicy = new SecPolicy(Collections.singletonList(vsy));

        boolean result = secPolicyResolver.deleteSecPolicy(secPolicy);
        System.out.println("执行结果是：" + result);
        Assertions.assertTrue(result);
    }

    /**
     * 查看安全策略的命中次数
     */
    @Test
    @Order(5)
    @DisplayName("查看安全策略的命中次数")
    void getSecPolicyHitTimes() throws IOException {
        //只查询「静态安全策略」的命中次数
//        VirtualSystem vsy = new VirtualSystem(DefaultVsysEnums.PUBLIC.getCode(), new StaticPolicy());
        VirtualSystem vsy = new VirtualSystem(DefaultVsysEnums.PUBLIC.getCode());
        SecPolicy secPolicy = new SecPolicy(Collections.singletonList(vsy));

        SecPolicy result = secPolicyResolver.getSecPolicyHitTimes(secPolicy);
        System.out.println("执行结果是：" + result);
        Assertions.assertNotNull(result);
    }
}