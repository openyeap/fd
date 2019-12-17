package ltd.fdsa.boot.starter.consul.autoconfig;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.log4j.Log4j2;
import ltd.fdsa.boot.starter.consul.properties.ConsulAuthProperties;
import ltd.fdsa.boot.starter.consul.properties.ConsulHealthCheckProperties;
import ltd.fdsa.boot.starter.consul.properties.ConsulProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

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
            ConsulAuthProperties consulAuthProperties = getConsulAuthProperties();
            if (consulAuthProperties.getEnabled()) {
                Map<String, String> urlRoles = createAuthKV();
                for (Map.Entry<String, String> entry : urlRoles.entrySet()) {
                    client.setKVValue(entry.getKey(), entry.getValue());
                }
            }
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
    private final String DEFAULT_CONSUL_HOST = "localhost";
    private final int DEFAULT_CONSUL_PORT = 8500;

    private ConsulProperties getConsulProperties() {
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
            consulProperties.setHost(DEFAULT_CONSUL_HOST);
        }
        if (consulProperties.getPort() == 0) {
            consulProperties.setPort(DEFAULT_CONSUL_PORT);
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

    private String getApplicationHost() {
        String applicationHost;
        try {
            InetAddress address = InetAddress.getLocalHost();
            applicationHost = address.getHostAddress();
        } catch (UnknownHostException e) {
            applicationHost = DEFAULT_CONSUL_HOST;
        }
        return applicationHost;
    }

    @Bean("consulClient")
    public ConsulClient createConsulClient() {
        ConsulProperties consulProperties = getConsulProperties();
        return new ConsulClient(consulProperties.getHost(), consulProperties.getPort());
    }

    /**
     * 创建服务对象，进行注册
     *
     * @return
     */
    private NewService createNewService() {
        String applicationHost = getApplicationHost();
        NewService service = new NewService();
        service.setAddress(applicationHost);
        service.setPort(applicationPort);
        service.setId(applicationHost + ":" + applicationPort + "/" + applicationName);
        service.setName(applicationName);

        ConsulHealthCheckProperties consulHealthCheckProperties = getConsulHealthCheckProperties();
        if (consulHealthCheckProperties.getEnabled()) {
            NewService.Check check = new NewService.Check();
            check.setHttp("http://" + applicationHost + ":" + applicationPort + consulHealthCheckProperties.getHealthCheckPath());
            check.setMethod("GET");
            check.setInterval(consulHealthCheckProperties.getHealthCheckInterval());
            check.setTimeout(consulHealthCheckProperties.getHealthCheckTimeout());
            service.setCheck(check);
        }
        return service;
    }

    @Autowired
    private ApplicationContext applicationContext;

    private final Boolean DEFAULT_AUTH_ENABLED = true;
    private final String DEFAULT_AUTH_BASE_PACKAGE = "ltd.fdsa";

    private ConsulAuthProperties getConsulAuthProperties() {
        ConsulAuthProperties consulAuthProperties;
        try {
            consulAuthProperties = Binder.get(environment).bind(ConsulProperties.prefix, ConsulAuthProperties.class).get();
        } catch (Exception e) {
            consulAuthProperties = null;
        }
        if (consulAuthProperties == null) {
            consulAuthProperties = new ConsulAuthProperties();
        }
        if (consulAuthProperties.getEnabled() == null) {
            consulAuthProperties.setEnabled(DEFAULT_AUTH_ENABLED);
        }
        if (StringUtils.isEmpty(consulAuthProperties.getBasePackage())) {
            consulAuthProperties.setBasePackage(DEFAULT_AUTH_BASE_PACKAGE);
        }
        return consulAuthProperties;
    }

    private Map<String, String> createAuthKV() {
        Map<String, String> urlRoles = new HashMap<>();

        String kvPrefix = "auth/" + applicationName + "/";

        Map<String, Object> restControllers = applicationContext.getBeansWithAnnotation(RestController.class);
        if (restControllers == null || restControllers.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Object> entry : restControllers.entrySet()) {
            //用于装载类上的RequestMapping的value，{"depart","dep"}
            String[] urlClasss = null;
            RequestMapping rmForClass = entry.getValue().getClass().getAnnotation(RequestMapping.class);
            if (rmForClass != null) {
                urlClasss = rmForClass.value();
            }
            //获取类中方法上的注解
            Method[] methods = entry.getValue().getClass().getMethods();
            for (Method method : methods) {
                //1、获取权限注解
                //TODO:实现获取权限注解
                String role = "admin,user";

                //2、获取方法路径
                String[] urlMethods = null;
                //目前支持RequestMapping、PostMapping、GetMapping,其余暂不支持
                RequestMapping rmForMethod = method.getAnnotation(RequestMapping.class);
                if (rmForMethod != null) {
                    urlMethods = rmForMethod.value();
                } else {
                    PostMapping pmForMethod = method.getAnnotation(PostMapping.class);
                    if (pmForMethod != null) {
                        urlMethods = pmForMethod.value();
                    } else {
                        GetMapping gmForMethod = method.getAnnotation(GetMapping.class);
                        if (gmForMethod != null) {
                            urlMethods = gmForMethod.value();
                        }
                    }
                }
                if (urlMethods == null) {
                    continue;
                }

                //  auth/applicationName/urlClass/urlMethod --> role
                for (String urlMethod : urlMethods) {
                    if (urlClasss == null) {
                        urlRoles.put(kvPrefix + urlMethod, role);
                    } else {
                        for (String urlClass : urlClasss) {
                            urlRoles.put(kvPrefix + urlClass + "/" + urlMethod, role);
                        }
                    }
                }
            }
        }
        return urlRoles;
    }


}
