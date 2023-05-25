package cn.zhumingwu.dataswitch.core.job.handler.annotation;
import java.lang.annotation.*;

/**
 * annotation for method jobhandler
 *
 * @author  
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JobHandler {

    /**
     * handler name
     */
    String value();

    /**
     * init handler, invoked when JobThread init
     */
    String init() default "";

    /**
     * destroy handler, invoked when JobThread destroy
     */
    String destroy() default "";

}


