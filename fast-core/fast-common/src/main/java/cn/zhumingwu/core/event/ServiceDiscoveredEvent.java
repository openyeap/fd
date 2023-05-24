package cn.zhumingwu.core.event;
 
import cn.zhumingwu.core.service.ServiceInfo;
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
    private final Map<String, List<ServiceInfo>> services;

    public ServiceDiscoveredEvent(Object source, Map<String, List<ServiceInfo>> services) {
        super(source);
        this.services = services;
    }

    public Map<String, List<ServiceInfo>> getServices() {
        return services;
    }

}
