package ltd.fdsa.starter.remote.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = RpcProperties.PREFIX)
public class RpcProperties {
    public static final String PREFIX = "project.rpc";

    private boolean enabled;

    private String[] packages;
}
