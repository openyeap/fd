package cn.zhumingwu.base.event;
 
import cn.zhumingwu.base.service.InstanceInfo;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

/**
 * 服务发现: Name，IPAddress, Port, Metas
 * Metas: Id、Type、Schema、Node...
 * 服务类型: Restful API、Rpc API(eg. job handle)、Web Socket
 * @author zhumingwu
 *
 * @since 4/4/2021 3:46 PM
 */
@ToString
public class ServiceDiscoveredEvent extends ApplicationEvent {
    private final Map<String, List<InstanceInfo>> services;

    public ServiceDiscoveredEvent(Object source, Map<String, List<InstanceInfo>> services) {
        super(source);
        this.services = services;
    }

    public Map<String, List<InstanceInfo>> getServices() {
        return services;
    }

}
