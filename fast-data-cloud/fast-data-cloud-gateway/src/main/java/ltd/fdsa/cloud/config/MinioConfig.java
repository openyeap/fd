package ltd.fdsa.cloud.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.property.MinioProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioProperties properties) {
        try {
            return new MinioClient(properties.getUrl(), properties.getAccessKey(), properties.getSecretKey());
        } catch (InvalidEndpointException e) {
            log.error("", e);
        } catch (InvalidPortException e) {
            log.error("", e);
        }
        return null;
    }
}
