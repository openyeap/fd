package cn.zhumingwu.consul.properties;

import com.ecwid.consul.transport.TLSConfig;
import lombok.Data;
import lombok.ToString;
import cn.zhumingwu.base.service.InstanceInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@Data
@ConfigurationProperties(prefix = ConsulProperties.PREFIX)
@ToString
public class ConsulProperties {
    public static final String PREFIX = "project.consul";
    /**
     * default consul api path.
     */
    private String path = "";
    /**
     * default consul acl Token.
     */
    private String aclToken;
    /**
     * default consul host include http schema.
     */
    private String host;
    /**
     * default consul port.
     */
    private int port = 8500;

    private TLSConfig tlsConfig;

    private ServiceRegistry registry = new ServiceRegistry();

    private ServiceWatch serviceWatch = new ServiceWatch();

    private EventWatch eventWatch = new EventWatch();

    private ConfigWatch configWatch = new ConfigWatch();

    private HealthCheck healthCheck = new HealthCheck();

    /**
     * Consul service register.
     */
    @Data
    @ToString
    public static class ServiceRegistry {
        private boolean enabled = true;
        /**
         * The value of the fixed delay for the register in Minutes. Defaults to 10.
         */
        private Duration delay = Duration.ofMinutes(10);

        private List<InstanceInfo> services;
    }

    /**
     * Consul watch properties.
     */
    @Data
    @ToString
    public static class HealthCheck {

        private String path = "";
        private String method = "GET";
        /**
         * If the watch is enabled. Defaults to true.
         */
        private boolean enabled = true;
 
        private Duration waitTime = Duration.ofSeconds(5);
 
        private Duration delay = Duration.ofSeconds(10);

        private Duration removeAfter =Duration.ofSeconds(5);
    }


    /**
     * Consul watch properties.
     */
    @Data
    @ToString
    public static class EventWatch {
        private boolean enabled = true;
        /**
         * The number of seconds to wait (or block) for watch query, defaults to 55. Needs
         * to be less than default ConsulClient (defaults to 60). To increase ConsulClient
         * timeout create a ConsulClient bean with a custom ConsulRawClient with a custom
         * HttpClient.
         */
        private Duration waitTime = Duration.ofSeconds(55);

        /**
         * The default value of the fixed delay for the event watch in millis is 10.
         */
        private Duration delay = Duration.ofMillis(10);

    }

    /**
     * Consul watch properties.
     */
    @Data
    @ToString
    public static class ServiceWatch {
        /**
         * If the watch is enabled. Defaults to true.
         */
        private boolean enabled = true;
        /**
         * The number of seconds to wait (or block) for watch query, defaults to 55. Needs
         * to be less than default ConsulClient (defaults to 60). To increase ConsulClient
         * timeout create a ConsulClient bean with a custom ConsulRawClient with a custom
         * HttpClient.
         */
        private Duration waitTime = Duration.ofSeconds(55);


        /**
         * The default value of the fixed delay for the service watch in Minutes is 1.
         */
        private Duration delay = Duration.ofMinutes(1);
    }

    /**
     * Consul watch properties.
     */
    @Data
    @ToString
    public static class ConfigWatch {
        /**
         * If the watch is enabled. Defaults to true.
         */
        private boolean enabled = true;

        /**
         * The number of seconds to wait (or block) for watch query, defaults to 55. Needs
         * to be less than default ConsulClient (defaults to 60). To increase ConsulClient
         * timeout create a ConsulClient bean with a custom ConsulRawClient with a custom
         * HttpClient.
         */
        private Duration waitTime = Duration.ofSeconds(55);

        /**
         * The default value of the fixed delay for the cofig watch in millis is 100.
         */
        private Duration delay = Duration.ofMillis(100);

        /**
         * ${keyPrefix}/${name:${spring.application.name}} to use in looking up values in consul KV.
         */
        private String keyPrefix = "config";
    }
}
