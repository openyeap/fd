package cn.zhumingwu.server.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.core.event.RemotingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;

@Configuration
@Order(-100)
@Slf4j
public class DefaultRemotingEventApplicationListener implements ApplicationListener<RemotingEvent> {


    @SneakyThrows
    @Override
    public void onApplicationEvent(RemotingEvent event) {
        log.info(event.toString());
        var clazz = (Class<ApplicationEvent>) ClassUtils.forName(event.getName(), null);
        var applicationEvent = RemotingEvent.getApplicationEvent(event.getPayload(), clazz);
        log.info(applicationEvent.toString());
    }
}
