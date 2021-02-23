package cn.zifangsky.netconf.adapter.huawei.securityZone;

import cn.zifangsky.netconf.adapter.huawei.securityZone.model.SecurityZone;
import cn.zifangsky.netconf.adapter.huawei.securityZone.model.ZoneInstance;

import java.io.IOException;
import java.util.List;

/**
 * 安全区域相关方法
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
public interface SecurityZoneResolver {
    /**
     * 创建安全区域
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param zoneInstanceList 新的安全区域
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean createSecurityZone(List<ZoneInstance> zoneInstanceList) throws IOException;

    /**
     * 修改安全区域（修改时会根据zone-instance的名称匹配已有的安全区域）
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param zoneInstanceList 安全区域
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean editSecurityZone(List<ZoneInstance> zoneInstanceList) throws IOException;

    /**
     * 查看安全区域
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param filter 筛选条件
     * @return 结果
     * @throws IOException If there are errors communicating with the netconf server.
     */
    List<ZoneInstance> getSecurityZone(SecurityZone filter) throws IOException;

    /**
     * 删除安全区域（删除时只需要提供zone-instance的名称即可）
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param zoneInstanceList 需要删除的安全区域
     * @return boolean
     * @throws IOException If there are errors communicating with the netconf server.
     */
    boolean deleteSecurityZone(List<ZoneInstance> zoneInstanceList) throws IOException;
}