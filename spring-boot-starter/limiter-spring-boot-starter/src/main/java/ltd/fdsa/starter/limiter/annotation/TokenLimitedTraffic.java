package ltd.fdsa.starter.limiter.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface TokenLimitedTraffic {
}
