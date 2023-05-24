package cn.zhumingwu.starter.remote.annotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 修饰：域、参数、方法
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/**
 * 限定注解
 */
@Qualifier
public @interface RpcApis {

    @AliasFor("service")
    String value() default "";

    @AliasFor("value")
    String service() default "";
}