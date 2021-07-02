package ltd.fdsa.redis.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = RedisConfigProperties.PREFIX)
@ToString
public class RedisConfigProperties {
    public static final String PREFIX = "project.redis";

    private boolean enabled = true;
    /**
     * The value of the fixed delay for the register in Minutes is 10.
     */
    private Duration delay = Duration.ofMinutes(10);
    /**
     * Alternative to spring.application.name to use in looking up values in consul KV.
     */
    @Value("${spring.application.name:default}")
    private String name;

    @Value("${spring.application.host:}")
    private String applicationHost;

    @Value("${server.port:8080}")
    private int applicationPort;

    private String cacheKey;

    private Duration cacheTtl = Duration.ofMinutes(30L);

    /**
     * The default value of the wait time for request watch in Seconds is 55.
     */
    private Duration waitTime = Duration.ofSeconds(55);

    private EventWatch eventWatch;

    private ConfigWatch configWatch;

    private ServiceWatch serviceWatch;

    @Data
    @ToString
    public class EventWatch {
        private boolean enabled = true;
        private String keyPrefix = "event:";

        /**
         * The default value of the wait time for request watch in Seconds is 55.
         */
        private Duration waitTime = Duration.ofSeconds(55);

        /**
         * The default value of the fixed delay for the event watch in millis is 10.
         */
        private Duration delay = Duration.ofMillis(10);
    }

    @Data
    @ToString
    public class ConfigWatch {
        private boolean enabled = true;
        private String keyPrefix = "config:";
        /**
         * The default value of the fixed delay for the config watch in millis is 100.
         */
        private Duration delay = Duration.ofMillis(100);
    }

    @Data
    @ToString
    public class ServiceWatch {
        private boolean enabled = true;
        private String keyPrefix = "service:";
        /**
         * The default value of the fixed delay for the config watch in Minutes is 10.
         */
        private Duration delay = Duration.ofMinutes(100);
    }

}