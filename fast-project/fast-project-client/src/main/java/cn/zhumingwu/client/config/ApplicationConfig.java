package cn.zhumingwu.client.config;

import cn.zhumingwu.database.config.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class ApplicationConfig {
    @Primary
    @Bean(name = "test")
    public DataSource dataSource(@Qualifier(DataSourceConfig.WRITER_DATASOURCE) DataSource dataSource) {
        return dataSource;
    }
}
