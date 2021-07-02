package ltd.fdsa.starter.remote.annotation;

import ltd.fdsa.starter.remote.config.RpcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({RpcAutoConfiguration.class})
public @interface EnableRpc {
}
