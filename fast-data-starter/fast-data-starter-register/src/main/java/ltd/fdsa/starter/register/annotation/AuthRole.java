package ltd.fdsa.starter.register.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Classname AuthRole
 * @Description TODO
 * @Date 2019/12/18 18:12
 * @Author 高进
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRole {
    String value() default "";
}
