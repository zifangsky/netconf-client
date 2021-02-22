package cn.zifangsky.netconf.adapter.huawei.rm;

import cn.zifangsky.netconf.adapter.huawei.rm.model.Rm;
import cn.zifangsky.netconf.adapter.huawei.rm.model.Route;

import java.io.IOException;
import java.util.List;

/**
 * 路由状态相关方法
 *
 * @author zifangsky
 * @date 2021/2/22
 * @since 1.0.0
 */
public interface RmResolver {
    /**
     * 获取路由状态
     * @author zifangsky
     * @date 2021/2/22
     * @since 1.0.0
     * @param rm 筛选条件
     * @return 结果
     * @throws IOException If there are errors communicating with the netconf server.
     */
    List<Route> getRouteState(Rm rm) throws IOException;
}