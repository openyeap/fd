package ltd.fdsa.starter.register.consul.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Classname ConsulHealthCheckProperties
 * @Description TODO
 * @Date 2019/12/16 17:00
 * @Author 高进
 */
@Data
@ConfigurationProperties(ConsulHealthCheckProperties.prefix)
public class ConsulHealthCheckProperties {
    public static final String prefix = "spring.cloud.consul.health";

    private Boolean enabled = true;

    private String healthCheckPath = "/actuator/health";
    private String healthCheckInterval = "10s";
    private String healthCheckTimeout = "1s";
    private String deRegisterCriticalServiceAfter = "30s";
}
