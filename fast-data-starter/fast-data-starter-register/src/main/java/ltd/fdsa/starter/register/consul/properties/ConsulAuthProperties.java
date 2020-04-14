package ltd.fdsa.starter.register.consul.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Classname ConsulAuthProperties
 * @Description TODO
 * @Date 2019/12/17 10:53
 * @Author 高进
 */
@Data
@ConfigurationProperties(ConsulAuthProperties.prefix)
public class ConsulAuthProperties {
    public static final String prefix = "spring.cloud.consul.auth";

    private Boolean enabled = true;

    private String basePackage = "ltd.fdsa";
}
