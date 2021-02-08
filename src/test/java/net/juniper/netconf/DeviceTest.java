package net.juniper.netconf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.juniper.netconf.adapter.huawei.HuaWeiConstants;
import net.juniper.netconf.adapter.huawei.secPolicy.SecPolicy;
import net.juniper.netconf.adapter.huawei.secPolicy.StaticPolicyGroup;
import net.juniper.netconf.adapter.huawei.secPolicy.VirtualSystem;
import net.juniper.netconf.core.DefaultRpcManager;
import net.juniper.netconf.core.Device;
import net.juniper.netconf.core.exception.NetconfException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("测试与防火墙的各种交互")
public class DeviceTest {

    private static final String TEST_HOSTNAME = "127.0.0.1";
    private static final String TEST_USERNAME = "netconf";
    private static final String TEST_PASSWORD = "admin123456";
    private static final int DEFAULT_NETCONF_PORT = 830;

    private static DefaultRpcManager rpcManager;

    @BeforeAll
    public static void init() throws NetconfException {
        Device device = Device.builder()
                .hostName(TEST_HOSTNAME)
                .userName(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .port(DEFAULT_NETCONF_PORT)
                .strictHostKeyChecking(false)
                .build();

        rpcManager = new DefaultRpcManager(device);
    }

    /**
     * 测试连接情况
     */
    @Test
    public void GIVEN_requiredParameters_THEN_createDevice() throws NetconfException {
        System.out.println(rpcManager.getSessionId() + ": " + rpcManager.isConnected());
    }

    /**
     * 查看安全策略配置
     */
    @Test
    public void getConfigSecPolicy() throws Exception {
        String xml = rpcManager.executeRpc("<get-config><source><running/></source><filter type=\"subtree\"><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"></sec-policy></filter></get-config>");
        System.out.println(xml);
    }

    /**
     * 创建安全策略配置
     */
    @Test
    public void editConfigCreateSecPolicy() throws Exception {
        String xml = rpcManager.executeRpc("<edit-config><target><running/></target><error-option>rollback-on-error</error-option><config><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\" xmlns:nc=\"urn:ietf:params:xml:ns:netconf:base:1.0\" xmlns:yang=\"urn:ietf:params:xml:ns:yang:1\"><vsys><name>public</name><static-policy><rule nc:operation='create'><name>test</name><desc>just for test</desc><source-zone>trust</source-zone><destination-zone>untrust</destination-zone><source-ip><address-ipv4>1.1.1.1/32</address-ipv4></source-ip><destination-ip><address-ipv4>2.2.2.2/32</address-ipv4></destination-ip><service><service-items><tcp><source-port>100 200 to 300 600</source-port><dest-port>700 888 to 999 1023</dest-port></tcp></service-items></service><action>true</action></rule></static-policy></vsys></sec-policy></config></edit-config>");
//        String xml = device.executeRPC("<edit-config><target><running/></target><error-option>rollback-on-error</error-option><config><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"><vsys><name>public</name><static-policy><rule nc:operation='create'><name>test2</name><desc>just for test2</desc><source-zone>trust</source-zone><destination-zone>untrust</destination-zone><source-ip><address-ipv4>1.1.1.1/32</address-ipv4></source-ip><destination-ip><address-ipv4>2.2.2.2/32</address-ipv4></destination-ip><service><service-items><tcp><source-port>100 200 to 300 600</source-port><dest-port>700 888 to 999 1023</dest-port></tcp></service-items></service><action>true</action></rule></static-policy></vsys></sec-policy></config></edit-config>");
        System.out.println(xml);
    }




    /**
     * 查询安全策略组
     */
    @Test
    public void getConfigSecPolicyGroup() throws Exception {
        SecPolicy secPolicy = new SecPolicy();
        List<VirtualSystem> vsys = new ArrayList<>();
        VirtualSystem vsy = new VirtualSystem();
        vsy.setName("public");
        vsy.setStaticPolicyGroup(new StaticPolicyGroup());

        vsys.add(vsy);
        secPolicy.setVsys(vsys);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String xmlStr = xmlMapper.writeValueAsString(secPolicy)
                .replaceAll(":?wstxns1:?", "")
                .replace("<sec-policy>", "<sec-policy xmlns=\"" + HuaWeiConstants.NAMESPACE_HUAWEI + "\">");
//        System.out.println(xmlStr);

        //执行
        String xml = rpcManager.getRunningConfig(xmlStr);
        System.out.println(xml);
    }


    @Test
    public void GIVEN_newDevice_WHEN_withNullUserName_THEN_throwsException() {
        assertThatThrownBy(() -> Device.builder().hostName("foo").build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("userName is marked @NonNull but is null");
    }

    @Test
    public void GIVEN_newDevice_WHEN_withHostName_THEN_throwsException() {
        assertThatThrownBy(() -> Device.builder().userName("foo").build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("hostName is marked @NonNull but is null");
    }
}
