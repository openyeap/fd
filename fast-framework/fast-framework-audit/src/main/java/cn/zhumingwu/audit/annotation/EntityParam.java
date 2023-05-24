package cn.zhumingwu.audit.annotation;

import java.lang.annotation.*;

/**
 * 控制器实体参数注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface EntityParam {
}
