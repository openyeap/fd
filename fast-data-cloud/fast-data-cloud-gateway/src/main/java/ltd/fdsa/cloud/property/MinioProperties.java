package ltd.fdsa.cloud.property;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = MinioProperties.PREFIX)
@ToString
public class MinioProperties {
    public static final String PREFIX = "project.minio";
    private String url;
    private String accessKey;
    private String secretKey;
}
