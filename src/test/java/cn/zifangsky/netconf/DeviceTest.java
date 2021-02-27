package cn.zifangsky.netconf;

import cn.zifangsky.netconf.adapter.huawei.HuaWeiConstants;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.SecPolicy;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.StaticPolicyGroup;
import cn.zifangsky.netconf.adapter.huawei.secPolicy.model.VirtualSystem;
import cn.zifangsky.netconf.core.*;
import cn.zifangsky.netconf.core.exception.NetconfException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DisplayName("测试与防火墙的各种交互")
public class DeviceTest {

    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private static RpcManager rpcManager;

    @BeforeAll
    public static void init() throws NetconfException {
        Device device = DefaultDevice.builder()
                .hostName(TEST_HOSTNAME)
                .userName(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .port(DEFAULT_NETCONF_PORT)
                .strictHostKeyChecking(false)
                .build();

        rpcManager = new SingleDeviceRpcManager(device);
    }

    /**
     * 测试连接情况
     */
    @Test
    @DisplayName("测试连接情况")
    public void GIVEN_requiredParameters_THEN_createDevice() throws IOException {
        System.out.println(rpcManager.getSessionId() + ": " + rpcManager.isConnected());
    }

    /**
     * 查看安全策略配置
     */
    @Test
    @DisplayName("查看安全策略配置")
    public void getConfigSecPolicy() throws Exception {
        String xml = rpcManager.executeRpc("<get-config><source><running/></source><filter type=\"subtree\"><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"></sec-policy></filter></get-config>");
        System.out.println(xml);
    }

    /**
     * 创建安全策略配置
     */
    @Test
    @DisplayName("创建安全策略配置")
    public void editConfigCreateSecPolicy() throws Exception {
        String xml = rpcManager.executeRpc("<edit-config><target><running/></target><error-option>rollback-on-error</error-option><config><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\" xmlns:nc=\"urn:ietf:params:xml:ns:netconf:base:1.0\" xmlns:yang=\"urn:ietf:params:xml:ns:yang:1\"><vsys><name>public</name><static-policy><rule nc:operation='create'><name>test</name><desc>just for test</desc><source-zone>trust</source-zone><destination-zone>untrust</destination-zone><source-ip><address-ipv4>1.1.1.1/32</address-ipv4></source-ip><destination-ip><address-ipv4>2.2.2.2/32</address-ipv4></destination-ip><service><service-items><tcp><source-port>100 200 to 300 600</source-port><dest-port>700 888 to 999 1023</dest-port></tcp></service-items></service><action>true</action></rule></static-policy></vsys></sec-policy></config></edit-config>");
//        String xml = device.executeRPC("<edit-config><target><running/></target><error-option>rollback-on-error</error-option><config><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"><vsys><name>public</name><static-policy><rule nc:operation='create'><name>test2</name><desc>just for test2</desc><source-zone>trust</source-zone><destination-zone>untrust</destination-zone><source-ip><address-ipv4>1.1.1.1/32</address-ipv4></source-ip><destination-ip><address-ipv4>2.2.2.2/32</address-ipv4></destination-ip><service><service-items><tcp><source-port>100 200 to 300 600</source-port><dest-port>700 888 to 999 1023</dest-port></tcp></service-items></service><action>true</action></rule></static-policy></vsys></sec-policy></config></edit-config>");
        System.out.println(xml);
    }

    /**
     * 删除安全策略配置
     */
    @Test
    @DisplayName("删除安全策略配置")
    public void deleteConfigCreateSecPolicy() throws Exception {
        String xml = rpcManager.executeRpc("<edit-config><target><running/></target><error-option>rollback-on-error</error-option><config><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\" xmlns:nc=\"urn:ietf:params:xml:ns:netconf:base:1.0\" xmlns:yang=\"urn:ietf:params:xml:ns:yang:1\"><vsys><name>public</name><static-policy><rule nc:operation=\"delete\"><name>test_by_code</name></rule></static-policy></vsys></sec-policy></config></edit-config>");
        System.out.println(xml);
    }

    /**
     * 查看安全策略的命中次数
     */
    @Test
    @DisplayName("查看安全策略的命中次数")
    public void getSecPolicyHitTimes() throws Exception {
        String xml = rpcManager.executeRpc("<get><filter type=\"subtree\"><sec-policy-state xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"><vsys><name>public</name><static-policy/></vsys></sec-policy-state></filter></get>");
        System.out.println(xml);
    }

    /**
     * 查询安全策略组
     */
    @Test
    @DisplayName("查询安全策略组")
    public void getConfigSecPolicyGroup() throws Exception {
        SecPolicy secPolicy = new SecPolicy();
        List<VirtualSystem> vsys = new ArrayList<>();
        VirtualSystem vsy = new VirtualSystem();
        vsy.setName("public");
        vsy.setStaticPolicyGroup(new StaticPolicyGroup());

        vsys.add(vsy);
        secPolicy.setVsys(vsys);

//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String xmlStr = AbstractExecutingRpcManager.XMLMAPPER_RESOURCES.get().writeValueAsString(secPolicy)
                .replace("<sec-policy>", "<sec-policy xmlns=\"" + HuaWeiConstants.URN_HUAWEI_SECURITY_POLICY + "\">");
//        System.out.println(xmlStr);

        //执行
        String xml = rpcManager.getRunningConfig(xmlStr);
        System.out.println(xml);
    }

    /**
     * 获取路由状态
     */
    @Test
    @DisplayName("获取路由状态")
    public void getRouteState() throws Exception {
        String xml = rpcManager.executeRpc("<get><filter type=\"subtree\"><rm xmlns=\"http://www.huawei.com/netconf/vrp/huawei-rm\" xmlns:nc=\"urn:ietf:params:xml:ns:netconf:base:1.0\" ></rm></filter></get>");
        System.out.println(xml);
    }

}
