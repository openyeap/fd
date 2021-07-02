package ltd.fdsa.starter.jdbc.config;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.database.service.BaseService;
import ltd.fdsa.starter.jdbc.controller.JdbcController;
import ltd.fdsa.starter.jdbc.properties.JdbcApiProperties;
import ltd.fdsa.starter.jdbc.service.JdbcService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@Configuration
@ConditionalOnWebApplication
@Slf4j
public class JdbcApiAutoConfiguration {

    @Bean
    public JdbcService jdbcService() {
        return new JdbcService();
    }

    @RestController
    @ConditionalOnProperty(value = JdbcApiProperties.PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
    static class JdbcControllerConfiguration extends JdbcController {
        {
            NamingUtils.formatLog(log, "JdbcControllerConfiguration Start");
        }
    }
}
