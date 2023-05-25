package cn.zhumingwu.dataswitch.admin.config;

import cn.zhumingwu.base.event.ServiceDiscoveredEvent;
import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.dataswitch.admin.scheduler.JobScheduler;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.var;
import org.hibernate.sql.OracleJoinFragment;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class JobAdminConfig implements ApplicationListener<ServiceDiscoveredEvent> {


    private JobScheduler JobScheduler;


    private static final ConcurrentMap<String, ExecutorHandler> executorHandlerConcurrentMap = new ConcurrentHashMap<String, ExecutorHandler>();

    @Override
    public void onApplicationEvent(ServiceDiscoveredEvent event) {
        var services = event.getServices();
        for (var service : services.entrySet()) {
            for (var instance : service.getValue()) {
                for (var mata : instance.getMetadata().entrySet()) {
                    var key = mata.getKey();
                    if (key.startsWith("JobHandler.")) {
                        //todo 获取最新
                        var name = service.getKey() + "." + key.substring("JobHandler.".length());
                        var instanceList = executorHandlerConcurrentMap.get(name);
                        if (instanceList == null) {
                            instanceList = new ExecutorHandler(new HashSet<InstanceInfo>());
                            instanceList.getInstances().add(instance);
                            executorHandlerConcurrentMap.put(name, instanceList);
                        } else {
                            instanceList.getInstances().add(instance);
                        }
                    }
                }
            }
        }
    }
}

@Data
class ExecutorHandler {
    private final Set<InstanceInfo> instances;

    ExecutorHandler(HashSet<InstanceInfo> instances) {
        this.instances = instances;
    }
}
