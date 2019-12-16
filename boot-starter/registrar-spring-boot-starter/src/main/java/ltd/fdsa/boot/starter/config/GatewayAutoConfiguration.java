package ltd.fdsa.boot.starter.config;

import com.ecwid.consul.v1.ConsulClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname GatewayAutoConfiguration
 * @Description TODO
 * @Date 2019/12/13 15:33
 * @Author 高进
 */
@Slf4j
@Configuration
@ConditionalOnBean(value = com.ecwid.consul.v1.ConsulClient.class)
public class GatewayAutoConfiguration implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("==========ApplicationStartedEvent ServiceRegister start===========");
        try {
            this.consulClient.getAgentServices().getValue().size();
            // TODO get all urls with role then update to consul kv store.
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
        log.info("=========ApplicationStartedEvent ServiceRegister end===========");
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ConsulClient consulClient;

}
