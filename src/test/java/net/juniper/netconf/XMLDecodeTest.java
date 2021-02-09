package net.juniper.netconf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import net.juniper.netconf.adapter.huawei.secPolicy.model.SecPolicyData;
import net.juniper.netconf.core.model.RpcReply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static net.juniper.netconf.core.DefaultRpcManager.XMLMAPPER_RESOURCES;

/**
 * 测试XML字符串转Java对象的各种情况
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@DisplayName("测试XML字符串转Java对象的各种情况")
public class XMLDecodeTest {

    /**
     * 执行成功标识
     */
    @Test
    @DisplayName("测试-执行成功标识")
    public void decodeRespOKXML() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"1001\"><ok/></rpc-reply>";

        RpcReply<Void> rpcReply = this.deserializeRpcReply(xml);
        System.out.println(rpcReply);
    }



    /**
     * 执行失败返回相应错误信息
     */
    @Test
    @DisplayName("测试-执行失败返回相应错误信息")
    public void decodeRespErrorXML() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"1001\"><rpc-error><error-type>application</error-type><error-tag>operation-failed</error-tag><error-severity>error</error-severity><error-path>/hw-secply:sec-policy/hw-secply:vsys[hw-secply:name=public]/hw-secply:static-policy/hw-secply:rule[hw-secply:name=test12]/hw-secply:source-zone</error-path><error-message>Failed to apply configuration changes to device.</error-message></rpc-error></rpc-reply>";

        RpcReply<Void> rpcReply = this.deserializeRpcReply(xml);
        System.out.println(rpcReply);
    }

    /**
     * 查看安全策略配置<响应报文>
     */
    @Test
    @DisplayName("测试-查看安全策略配置<响应报文>")
    public void decodeRpcReplyXML() throws Exception {
//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"1008\"><data><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"><vsys><name>public</name><default-policy><action>true</action></default-policy><static-policy></static-policy></vsys></sec-policy></data></rpc-reply>";
        String xml = "<rpc-reply xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"2\"><data><sec-policy xmlns=\"urn:huawei:params:xml:ns:yang:huawei-security-policy\"><vsys><name>public</name><default-policy><action>true</action><policylog>false</policylog><sessionlog>false</sessionlog><global-ip><destination>false</destination></global-ip></default-policy><static-policy><rule><name>test_policy_a</name><desc>测试安全策略A</desc><parent-group>策略组A</parent-group><source-ip><address-ipv4>192.168.1.100/32</address-ipv4></source-ip><destination-ip><address-ipv4>192.168.1.101/32</address-ipv4></destination-ip><service><service-object>测试服务A</service-object><service-object>https</service-object></service><time-range>2021-02-05~2021-02-28</time-range><profile><fileblock-profile>default</fileblock-profile></profile><enable>true</enable><action>true</action></rule></static-policy><static-policy-group><group><name>策略组A</name><rule-range><start-rule>test_policy_a</start-rule><end-rule>test_policy_a</end-rule></rule-range><enable>true</enable><description>这是用于测试的策略组A</description></group><group><name>策略组B</name><rule-range></rule-range><enable>true</enable><description>这是用于测试的策略组B</description></group></static-policy-group></vsys></sec-policy></data></rpc-reply>";

        RpcReply<SecPolicyData> rpcReply = XMLMAPPER_RESOURCES.get().readValue(xml, new TypeReference<RpcReply<SecPolicyData>>() {
        });
        System.out.println(rpcReply);
    }



    /**
     * 反序列化返回报文，获取返回报文的执行状态
     */
    private RpcReply<Void> deserializeRpcReply(String rpcReply) throws JsonProcessingException {
        return XMLMAPPER_RESOURCES.get().readValue(rpcReply, new TypeReference<RpcReply<Void>>() {
        });
    }

}
