package cn.zhumingwu.starter.logger.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    /**
     * 模块
     *
     * @return
     */
    String module();

    /**
     * 操作
     */
    String operate();

    /**
     * 描述信息 ***
     */
    String desc() default "";

    /**
     * 记录执行参数
     *
     * @return
     */
    boolean withParam() default true;
}
