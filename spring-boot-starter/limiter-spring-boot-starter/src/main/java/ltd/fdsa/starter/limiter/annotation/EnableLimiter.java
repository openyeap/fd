package ltd.fdsa.starter.limiter.annotation;

import ltd.fdsa.starter.limiter.config.LimiterAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({LimiterAutoConfiguration.class})
public @interface EnableLimiter {
}
