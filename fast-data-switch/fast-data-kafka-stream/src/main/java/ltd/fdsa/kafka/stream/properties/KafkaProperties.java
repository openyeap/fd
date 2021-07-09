package ltd.fdsa.kafka.stream.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = KafkaProperties.PREFIX)
public class KafkaProperties {

    public static final String PREFIX = "kafka";
    private String connectUrl;
    private String schemaRegistryUrl;
}
