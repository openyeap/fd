package ltd.fdsa.starter.limiter.config;

import ltd.fdsa.starter.limiter.annotation.EnableLimiter;
import ltd.fdsa.starter.limiter.aspect.LimiterAspect;
import ltd.fdsa.starter.limiter.initialize.LimiterInitialize;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

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
