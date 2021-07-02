package ltd.fdsa.switcher.core.job;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * annotation for method job handler
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Job {

    /**
     * job handler name
     */
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String cron() default "";
    /**
     * 任务结束后固定延迟执行
     */
    long fixedDelay() default -1;
    /**
     * 任务之间固定间隔执行
     */
    long fixedRate() default -1;

    /**
     * init handler, invoked when JobThread init
     */
    String init() default "";

    /**
     * destroy handler, invoked when JobThread destroy
     */
    String destroy() default "";
}
