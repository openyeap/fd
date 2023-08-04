package cn.zhumingwu.starter.limiter.config;

import cn.zhumingwu.starter.limiter.annotation.EnableLimiter;
import cn.zhumingwu.starter.limiter.initialize.LimiterInitialize;
import cn.zhumingwu.starter.limiter.aspect.LimiterAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.annotation.Resource;

@Configuration
@ConditionalOnBean(annotation = EnableLimiter.class)
public class LimiterAutoConfiguration {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Bean
    @ConditionalOnMissingBean(LimiterInitialize.class)
    public LimiterInitialize limiterInitialize() {
        return new LimiterInitialize(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(LimiterAspect.class)
    public LimiterAspect limiterAspect() {
        return new LimiterAspect(redisTemplate);
    }
}
