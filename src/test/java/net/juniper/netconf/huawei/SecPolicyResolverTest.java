package net.juniper.netconf.huawei;

import net.juniper.netconf.adapter.huawei.ActionEnums;
import net.juniper.netconf.adapter.huawei.secPolicy.SecPolicyResolver;
import net.juniper.netconf.adapter.huawei.secPolicy.SecPolicyResolverImpl;
import net.juniper.netconf.adapter.huawei.secPolicy.model.SecPolicy;
import net.juniper.netconf.adapter.huawei.secPolicy.model.StaticPolicy;
import net.juniper.netconf.adapter.huawei.secPolicy.model.VirtualSystem;
import net.juniper.netconf.adapter.huawei.secPolicy.model.rules.*;
import net.juniper.netconf.core.DefaultRpcManager;
import net.juniper.netconf.core.Device;
import net.juniper.netconf.core.exception.NetconfException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 测试--「华为防火墙-安全策略」
 *
 * @author zifangsky
 * @date 2021/2/9
 * @since 1.0.0
 */
@DisplayName("测试--「华为防火墙-安全策略」")
class SecPolicyResolverTest {
    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private static SecPolicyResolver secPolicyResolver;

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
        secPolicyResolver = new SecPolicyResolverImpl(rpcManager);
    }


    /**
     * 创建安全策略
     */
    @Test
    @DisplayName("创建安全策略")
    void createSecPolicy() throws IOException {
        SecPolicy secPolicy = new SecPolicy();
        List<VirtualSystem> vsys = new ArrayList<>();
        VirtualSystem vsy = new VirtualSystem();
        vsy.setName("public");

        StaticPolicy staticPolicy = new StaticPolicy();
        //一条新策略
        ServiceItems item = new ServiceItems();
        item.setTcp(new Tcp("100 200 to 300 600", "700 888 to 999 1023"));
        Rule newRule = new Rule("test_by_code", "通过程序自动下发配置", ActionEnums.TRUE, "untrust", "trust",
                new Address("1.1.1.1/32", true), new Address("1.1.1.2/32", true), new Service(item));

        staticPolicy.setRule(Collections.singletonList(newRule));
        vsy.setStaticPolicy(staticPolicy);
        vsys.add(vsy);
        secPolicy.setVsys(vsys);

        boolean result = secPolicyResolver.createSecPolicy(secPolicy);
        System.out.println("执行结果是：" + result);
    }
}