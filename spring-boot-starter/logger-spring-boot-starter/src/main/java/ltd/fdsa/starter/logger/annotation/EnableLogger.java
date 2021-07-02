package ltd.fdsa.starter.logger.annotation;

import ltd.fdsa.starter.logger.config.LoggingAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({LoggingAutoConfiguration.class})
public @interface EnableLogger {
}
