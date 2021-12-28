package ltd.fdsa.starter.jdbc.config;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.starter.jdbc.controller.JdbcController;
import ltd.fdsa.database.properties.JdbcApiProperties;
import ltd.fdsa.database.service.JdbcApiService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@Configuration
@Slf4j
@EnableConfigurationProperties(JdbcApiProperties.class)
public class JdbcApiAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = JdbcApiProperties.PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
    public JdbcApiService jdbcService(JdbcApiProperties properties, DataSource dataSource) {
        return new JdbcApiService(properties, dataSource);
    }

    @RestController
    @ConditionalOnProperty(value = JdbcApiProperties.PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
    public static class JdbcControllerConfiguration extends JdbcController {
        {
            NamingUtils.formatLog(log, "JdbcControllerConfiguration Start");
        }
    }
}
