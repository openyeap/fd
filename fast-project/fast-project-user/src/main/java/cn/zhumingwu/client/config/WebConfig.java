package cn.zhumingwu.client.config;

import cn.zhumingwu.base.token.SimpleWebTokenGenerator;
import cn.zhumingwu.base.token.SimpleWebTokenGeneratorImpl;
import cn.zhumingwu.base.token.SimpleWebTokenValidation;
import cn.zhumingwu.base.token.SimpleWebTokenValidationImpl;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.concurrent.Callable;

@Configuration
public class WebConfig {

    @Bean
    public SimpleWebTokenGenerator swtGenerator() {
        return new SimpleWebTokenGeneratorImpl(20, "");
    }

    @Bean
    public SimpleWebTokenValidation swtValidation() {
        return new SimpleWebTokenValidationImpl(20, "");
    }

    @Bean
    public JdbcTemplate getTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate getParTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public Cache dis() {
       return new Cache() {
           @Override
           public String getName() {
               return null;
           }

           @Override
           public Object getNativeCache() {
               return null;
           }

           @Override
           public ValueWrapper get(Object key) {
               return null;
           }

           @Override
           public <T> T get(Object key, Class<T> type) {
               return null;
           }

           @Override
           public <T> T get(Object key, Callable<T> valueLoader) {
               return null;
           }

           @Override
           public void put(Object key, Object value) {

           }

           @Override
           public void evict(Object key) {

           }

           @Override
           public void clear() {

           }
       };
    }
}
