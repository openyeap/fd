package ltd.fdsa.boot.starter.consul.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * @Classname ConsulProperties
 * @Description TODO
 * @Date 2019/12/16 16:10
 * @Author 高进
 */
@Data
@ConfigurationProperties(ConsulProperties.prefix)
public class ConsulProperties {
    public static final String prefix = "spring.cloud.consul";

    private String host = "localhost";

    private int port = 8500;

    public String toString() {
        return "ConsulProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
