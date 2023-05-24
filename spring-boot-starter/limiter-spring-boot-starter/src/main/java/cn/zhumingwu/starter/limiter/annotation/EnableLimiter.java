package cn.zhumingwu.starter.limiter.annotation;

import cn.zhumingwu.starter.limiter.config.LimiterAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({LimiterAutoConfiguration.class})
public @interface EnableLimiter {
}
