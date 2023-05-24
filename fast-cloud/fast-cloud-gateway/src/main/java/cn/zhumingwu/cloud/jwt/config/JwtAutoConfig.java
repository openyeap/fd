package cn.zhumingwu.cloud.jwt.config;

import cn.zhumingwu.cloud.jwt.IJwtToken;
import cn.zhumingwu.cloud.jwt.JwtProperties;
import cn.zhumingwu.cloud.jwt.impl.JwtTokenImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JwtAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public IJwtToken jwtToken(JwtProperties jwtProperties) {
        return new JwtTokenImpl(jwtProperties);
    }
}
