package ltd.fdsa.cloud.config;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.ecwid.consul.v1.ConsulClient;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j; 

@Component
@Configuration
@Slf4j
public class MinioConfig {

	@Value("${spring.Minio.endpoint:localhost}")
	private String endpoint;

	@Value("${spring.Minio.accessKey:localhost}")
	private String accessKey;

	@Value("${spring.Minio.secretKey:secretKey}")
	private String secretKey;

	@Value("${spring.Minio.String region:8500}")
	private String region;

	@Bean(name = "minioClient")

	@SneakyThrows({ InvalidEndpointException.class, InvalidPortException.class })
	public MinioClient createMinioClient() {
		MinioClient client = new MinioClient(this.endpoint, this.accessKey, this.secretKey, this.region);
		return client;
	}
}
