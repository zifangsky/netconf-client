package cn.zifangsky.netconf.adapter.huawei.secPolicy.model.rules;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 源/目的地址信息
 *
 * @author zifangsky
 * @date 21/2/4
 * @since 1.0.0
 */
@Data
public class Address {
    public static final String MASK_SEPARATOR = "/";
    public static final String DEFAULT_IPV4_MASK_32  = "/32";
    public static final String DEFAULT_IPV6_MASK_128  = "/128";

    /**
     * 表示地区，如BeiJing。
     */
    @JacksonXmlProperty(localName = "region")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> region;

    /**
     * 表示源/目的地址引用的地址(组)对象的名称。
     */
    @JacksonXmlProperty(localName = "address-set")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> addressSet;

    /**
     * 表示配置IPv4地址，点分十进制格式，与掩码之间使用“/”区分，掩码使用0-32的整数表示，如192.168.1.0/24。
     */
    @JacksonXmlProperty(localName = "address-ipv4")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> addressIpv4;

    /**
     * 表示配置IPv6地址，用IPv6地址用16进制表示，四位一组，中间用:隔开，全零的组可以用::来表示。前缀用十进制表示，地址与前缀之间使用“/”区分，前缀使用1-128的整数表示。如1:2:3::4:5/120。
     */
    @JacksonXmlProperty(localName = "address-ipv6")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> addressIpv6;

    /**
     * 表示IPv4地址段节点，仅用于容纳子节点，自身无数据含义。
     */
    @JacksonXmlProperty(localName = "address-ipv4-range")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<AddressIpv4Range> addressIpv4Range;

    /**
     * 表示IPv6地址段节点，仅用于容纳子节点，自身无数据含义。
     */
    @JacksonXmlProperty(localName = "address-ipv6-range")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<AddressIpv6Range> addressIpv6Range;

    /**
     * 表示从源地址条件中排除某一个地址集。当排除一个地址集后，此地址集中的地址都不会命中此策略。
     */
    @JacksonXmlProperty(localName = "address-set-exclude")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> addressSetExclude;

    /**
     * 表示从源地址条件中排除一个IPv4子网。当排除一个子网后，此子网中的地址都不会命中此策略。
     */
    @JacksonXmlProperty(localName = "address-ipv4-exclude")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> addressIpv4Exclude;

    /**
     * 表示从源地址条件中排除一个IPv6子网。当排除一个子网后，此子网中的地址都不会命中此策略。
     */
    @JacksonXmlProperty(localName = "address-ipv6-exclude")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> addressIpv6Exclude;

    /**
     * 表示从源地址条件中排除一个IPv4地址段。当排除一个地址段后，此地址段中的地址都不会命中此策略。
     */
    @JacksonXmlProperty(localName = "address-ipv4-range-exclude")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<AddressIpv4RangeExclude> addressIpv4RangeExclude;

    /**
     * 表示从源地址条件中排除一个IPv6地址段。当排除一个地址段后，此地址段中的地址都不会命中此策略。
     */
    @JacksonXmlProperty(localName = "address-ipv6-range-exclude")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<AddressIpv6RangeExclude> addressIpv6RangeExclude;

    /**
     * 表示MAC地址，如11:11:22:22:33:33。
     */
    @JacksonXmlProperty(localName = "address-mac")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> addressMac;

    /**
     * 表示源地址引用的域名组对象的名称。
     */
    @JacksonXmlProperty(localName = "domain-set")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> domainSet;

    public Address() {
    }

    public Address(List<String> addressIp, boolean ipv4) {
        List<String> list = null;
        if(addressIp != null && addressIp.size() > 0){
            list = new LinkedList<>();

            for(String address : addressIp){
                //处理掩码/前缀
                if(!address.contains(MASK_SEPARATOR)){
                    address = (ipv4 ? (address + DEFAULT_IPV4_MASK_32) : (address + DEFAULT_IPV6_MASK_128));
                }
                list.add(address);
            }
        }

        if(ipv4){
            this.addressIpv4 = list;
        }else{
            this.addressIpv6 = list;
        }
    }

    /* 其他内部属性 */
    /**
     * IPv4地址段节点
     */
    @Data
    public static class AddressIpv4Range {
        /**
         * 表示IPv4地址段的起始地址，点分十进制格式，不需要掩码，如192.168.1.1。与下文<end-ipv4>标签必须成对出现。
         */
        @JacksonXmlProperty(localName = "start-ipv4")
        private String startIpv4;

        /**
         * 表示IPv4地址段的结束地址，点分十进制格式，不需要掩码，如192.168.1.2。与上文<start-ipv4>标签必须成对出现。
         */
        @JacksonXmlProperty(localName = "end-ipv4")
        private String endIpv4;
    }

    /**
     * IPv6地址段节点
     */
    @Data
    public static class AddressIpv6Range {
        /**
         * 表示IPv6地址段的起始地址，用十六进制表示，四位一组，中间用:隔开，全零的组可以用::来表示。与下文<end-ipv6>标签必须成对出现。如1:2:3::4:1。
         */
        @JacksonXmlProperty(localName = "start-ipv6")
        private String startIpv6;

        /**
         * 表示IPv6地址段的结束地址，用十六进制表示，四位一组，中间用:隔开，全零的组可以用::来表示。与下文<start-ipv6>标签必须成对出现。如1:2:3::4:2。
         */
        @JacksonXmlProperty(localName = "end-ipv6")
        private String endIpv6;
    }

    /**
     * 表示从源地址条件中排除一个IPv4地址段。当排除一个地址段后，此地址段中的地址都不会命中此策略。
     */
    @Data
    public static class AddressIpv4RangeExclude {
        /**
         * 表示地址段起始地址。
         */
        @JacksonXmlProperty(localName = "start-ipv4")
        private String startIpv4;

        /**
         * 表示地址段结束地址。
         */
        @JacksonXmlProperty(localName = "end-ipv4")
        private String endIpv4;
    }

    /**
     * 表示从源地址条件中排除一个IPv6地址段。当排除一个地址段后，此地址段中的地址都不会命中此策略。
     */
    @Data
    public static class AddressIpv6RangeExclude {
        /**
         * 表示地址段起始地址。
         */
        @JacksonXmlProperty(localName = "start-ipv6")
        private String startIpv6;

        /**
         * 表示地址段结束地址。
         */
        @JacksonXmlProperty(localName = "end-ipv6")
        private String endIpv6;
    }
}
