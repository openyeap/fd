package cn.zhumingwu.starter.jdbc.config;

import cn.zhumingwu.database.properties.JdbcApiProperties;
import cn.zhumingwu.database.service.JdbcApiService;
import cn.zhumingwu.starter.jdbc.controller.JdbcApiController;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.core.util.NamingUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
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

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL).build();
    }

    @RestController
    @ConditionalOnProperty(value = JdbcApiProperties.PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
    public static class JdbcControllerConfiguration extends JdbcApiController {
        {
            NamingUtils.formatLog(log, "JdbcControllerConfiguration Start");
        }
    }
}
