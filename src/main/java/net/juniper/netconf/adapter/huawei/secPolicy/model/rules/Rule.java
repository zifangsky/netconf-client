package net.juniper.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import net.juniper.netconf.adapter.huawei.ActionEnums;

/**
 * <p>表示安全策略的规则，仅用于容纳子节点，自身无数据意义。</p>
 * <p>标签对<rule></rule>之间定义一条安全策略规则rule。</p>
 * <p>安全策略规则可以包含源目安全域、源目地址、服务、应用、时间段等多种策略匹配条件，来对流量进行精细化匹配过滤。</p>
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
public class Rule {
    /**
     * 表示安全策略规则的名称，一条规则只有一个名称，长度为1~32字节。在操作某条规则时必填。
     */
    @JacksonXmlProperty(localName = "name")
    private String name;

    /**
     * 表示安全策略的规则的描述信息，一条规则只有一个，长度为1~128字节，为可选项。
     */
    @JacksonXmlProperty(localName = "desc")
    private String desc;

    /**
     * 所属策略组
     */
    @JacksonXmlProperty(localName = "parent-group")
    private String parentGroup;

    /**
     * 表示安全策略规则的动作。取值"true"，表示允许匹配该规则的流量通过；取值"false"，表示禁止匹配该规则的流量通过。如果该动作未赋值，则该策略规则不生效。
     */
    @JacksonXmlProperty(localName = "action")
    private ActionEnums action;

    /**
     * 表示安全策略规则的使能状态，取值"true"表示使能，取值"false"表示不使能。默认值是使能。
     */
    @JacksonXmlProperty(localName = "enable")
    private Boolean enable;

    /**
     * 表示在安全策略阻断流量时，向源或者目的发送反馈报文。
     */
    @JacksonXmlProperty(localName = "send-deny-packet")
    private SendDenyPacketEnums sendDenyPacket;

    /**
     * 表示安全策略规则引用的源安全区域的名称，为可选项。源安全区域是指流量发出的安全区域。安全区域包括系统缺省存在和用户自定义的安全区域。
     */
    @JacksonXmlProperty(localName = "source-zone")
    private String sourceZone;

    /**
     * 表示安全策略规则引用的目的安全区域的名称，为可选项。目的安全区域是指流量去往的安全区域。安全区域包括系统缺省存在和用户自定义的安全区域。
     */
    @JacksonXmlProperty(localName = "destination-zone")
    private String destinationZone;

    /**
     * 表示安全策略的规则引用的源地址信息，该节点仅用于容纳子节点，自身无数据意义，一条安全规则中最多只能出现1次，可选项。该节点的子节点可以是IP地址、IP地址（组）对象、IP地址段、MAC地址。
     */
    @JacksonXmlProperty(localName = "source-ip")
    private Address sourceIp;

    /**
     * 表示安全策略的规则引用的目的地址信息，该节点仅用于容纳子节点，自身无数据意义，一条安全规则中最多只能出现1次，可选项。该节点的子节点可以是IP地址、IP地址（组）对象、IP地址段、MAC地址。
     */
    @JacksonXmlProperty(localName = "destination-ip")
    private Address destinationIp;

    /**
     * 表示安全策略规则引用的服务信息，该节点仅用于容纳子节点，自身无数据意义，一条安全规则中最多只能出现1次。该节点的子节点可以是服务、服务组对象，也可以指定服务规则（协议、端口等）
     */
    @JacksonXmlProperty(localName = "service")
    private Service service;

    /**
     * 表示安全策略规则引用应用信息，该节点仅用于容纳子节点，自身无数据意义，一条安全规则中最多只能出现1次，可选项。该节点的子节点可以是应用、应用组、应用大类、应用小类。
     */
    @JacksonXmlProperty(localName = "application")
    private Application application;

    /**
     * 表示安全策略规则引用应用信息，该节点仅用于容纳子节点，自身无数据意义，一条安全规则中最多只能出现1次，可选项。该节点的子节点可以是应用、应用组、应用大类、应用小类。
     */
    @JacksonXmlProperty(localName = "source-user")
    private SourceUser sourceUser;

    /**
     * 表示安全策略规则引用的时间段，通过名称引用。
     */
    @JacksonXmlProperty(localName = "time-range")
    private String timeRange;

    /**
     * 表示安全策略的策略命中日志开关，取值"true"表示记录，取值"false"表示不记录。默认值是不记录。
     */
    @JacksonXmlProperty(localName = "policy-log")
    private Boolean policyLog;

    /**
     * 表示安全策略的会话日志开关，取值"true"表示记录，取值"false"表示不记录。默认值是不记录。
     */
    @JacksonXmlProperty(localName = "session-log")
    private Boolean sessionLog;

    /**
     * 表示安全策略规则配置基于策略的会话老化时间。整数形式，取值范围为1～65535，单位为秒。
     */
    @JacksonXmlProperty(localName = "session-aging-time")
    private Integer sessionAgingTime;

    /**
     * 表示安全策略规则配置长连接，该节点仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "long-connection")
    private LongConnection longConnection;

    /**
     * 表示安全策略规则配置长连接，该节点仅用于容纳子节点，自身无数据意义。
     */
    @JacksonXmlProperty(localName = "profile")
    private Profile profile;

    /**
     * 表示安全策略的命中次数。
     */
    @JacksonXmlProperty(localName = "hit-times")
    private Long hitTimes;

    public Rule() {
    }

    public Rule(String name) {
        this.name = name;
    }

    public Rule(String name, String desc, ActionEnums action, String sourceZone, String destinationZone, Address sourceIp, Address destinationIp, Service service) {
        this.name = name;
        this.desc = desc;
        this.action = action;
        this.sourceZone = sourceZone;
        this.destinationZone = destinationZone;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.service = service;
    }
}
