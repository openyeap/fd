package cn.zhumingwu.core.lock;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WithLock {


    /**
     * 默认使用的lock名称
     *
     * @return String
     */
    String lockManager() default "";

    /**
     * lock 键值
     *
     * @return String
     */
    String lockKey() default "lockKey";

    long timeOutSeconds() default 0;
}