package ltd.fdsa.boot.starter.consul.autoconfig;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import lombok.extern.log4j.Log4j2;
import ltd.fdsa.boot.starter.consul.properties.ConsulHealthCheckProperties;
import ltd.fdsa.boot.starter.consul.properties.ConsulProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @Classname ConsulAutoConfiguration
 * @Description TODO
 * @Date 2019/12/16 16:10
 * @Author 高进
 */
@Configuration
@Log4j2
public class ConsulAutoConfiguration implements ApplicationListener<ApplicationStartedEvent>, EnvironmentAware {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("==========ApplicationStartedEvent ServiceRegister start===========");
        try {


            ConsulClient client = this.createConsulClient();
            client.agentServiceRegister(this.createNewService());
            // TODO get all urls with role then update to consul kv store.
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
        log.info("=========ApplicationStartedEvent ServiceRegister end===========");
    }

    private Environment environment;

    @Value("${spring.application.name:" + DEFAULT_APPLICATION_NAME + "}")
    private String applicationName;

    @Value("${server.port:8080}")
    private int applicationPort;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private final String DEFAULT_APPLICATION_NAME = "Default";
    private final String DEFAULT_HOST = "localhost";
    private final int DEFAULT_PORT = 8500;

    private ConsulProperties getConsulProperties() {
//        applicationName = environment.getProperty("spring.application.name");
//        if (applicationName == null || "".equals(applicationName)) {
//            applicationName = DEFAULT_APPLICATION_NAME;
//        }

        ConsulProperties consulProperties;
        try {
            consulProperties = Binder.get(environment).bind(ConsulProperties.prefix, ConsulProperties.class).get();
        } catch (Exception e) {
            consulProperties = null;
        }
        if (consulProperties == null) {
            consulProperties = new ConsulProperties();
        }
        if (StringUtils.isEmpty(consulProperties.getHost())) {
            consulProperties.setHost(DEFAULT_HOST);
        }
        if (consulProperties.getPort() == 0) {
            consulProperties.setPort(DEFAULT_PORT);
        }
        return consulProperties;
    }

    private final Boolean DEFAULT_HEALTH_CHECK_ENABLED = true;
    private final String DEFAULT_HEALTH_CHECK_PATH = "/actuator/health";
    private final String DEFAULT_HEALTH_CHECK_INTERVAL = "10s";
    private final String DEFAULT_HEALTH_CHECK_TIMEOUT = "1s";

    private ConsulHealthCheckProperties getConsulHealthCheckProperties() {
        ConsulHealthCheckProperties consulHealthCheckProperties;
        try {
            consulHealthCheckProperties = Binder.get(environment).bind(ConsulHealthCheckProperties.prefix, ConsulHealthCheckProperties.class).get();
        } catch (Exception e) {
            consulHealthCheckProperties = null;
        }

        if (consulHealthCheckProperties == null) {
            consulHealthCheckProperties = new ConsulHealthCheckProperties();
        }
        if (consulHealthCheckProperties.getEnabled() == null) {
            consulHealthCheckProperties.setEnabled(DEFAULT_HEALTH_CHECK_ENABLED);
        }
        if (StringUtils.isEmpty(consulHealthCheckProperties.getHealthCheckPath())) {
            consulHealthCheckProperties.setHealthCheckPath(DEFAULT_HEALTH_CHECK_PATH);
        }
        if (StringUtils.isEmpty(consulHealthCheckProperties.getHealthCheckInterval())) {
            consulHealthCheckProperties.setHealthCheckInterval(DEFAULT_HEALTH_CHECK_INTERVAL);
        }
        if (StringUtils.isEmpty(consulHealthCheckProperties.getHealthCheckTimeout())) {
            consulHealthCheckProperties.setHealthCheckTimeout(DEFAULT_HEALTH_CHECK_TIMEOUT);
        }
        return consulHealthCheckProperties;
    }

    @Bean("consulClient")
    public ConsulClient createConsulClient() {
        ConsulProperties consulProperties = getConsulProperties();
        return new ConsulClient(consulProperties.getHost(), consulProperties.getPort());
    }

    private NewService createNewService() {
        ConsulProperties consulProperties = getConsulProperties();
        NewService service = new NewService();
        service.setAddress(consulProperties.getHost());
        service.setPort(applicationPort);
        service.setId(consulProperties.getHost() + ":" + applicationPort + "/" + applicationName);
        service.setName(applicationName);

        ConsulHealthCheckProperties consulHealthCheckProperties = getConsulHealthCheckProperties();
        if (consulHealthCheckProperties.getEnabled()) {
            NewService.Check check = new NewService.Check();
            check.setHttp("http://" + consulProperties.getHost() + ":" + applicationPort + consulHealthCheckProperties.getHealthCheckPath());
            check.setMethod("GET");
            check.setInterval(consulHealthCheckProperties.getHealthCheckInterval());
            check.setTimeout(consulHealthCheckProperties.getHealthCheckTimeout());
            service.setCheck(check);
        }
        return service;
    }
}
