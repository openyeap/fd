package ltd.fdsa.starter.logger.properties;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(LoggingProperties.prefix)
@ToString
public class LoggingProperties {
    public static final String prefix = "logging";

    private DataSource datasource;

    private Logstash logstash;

    private Path webPath;

    private Map<String, String> level = new HashMap<>();

    @Data
    @ToString
    public static class Path {
        private String[] includePathPatterns;
        private String[] excludePathPatterns;
    }

    @Data
    @ToString
    public static class Logstash {
        private String destination;
        private int duration = 30;
    }

    @Data
    @ToString
    public static class DataSource {
        private String url;

        private String username;

        private String password;

        private String driverClassName;

    }
}
