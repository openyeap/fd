package cn.zhumingwu.core.metrics;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Metrics {
    /**
     * 在方法成功执行后打点，记录方法的执行时间发送到指标系统，默认开启
     *
     * @return boolean
     */
    boolean recordSuccessMetrics() default true;

    /**
     * 在方法成功失败后打点，记录方法的执行时间发送到指标系统，默认开启
     *
     * @return boolean
     */
    boolean recordFailMetrics() default true;

    /**
     * 通过日志记录请求参数，默认开启
     *
     * @return boolean
     */
    boolean logParameters() default true;

    /**
     * 通过日志记录方法返回值，默认开启
     *
     * @return boolean
     */
    boolean logReturn() default true;

    /**
     * 出现异常后通过日志记录异常信息，默认开启
     *
     * @return boolean
     */
    boolean logException() default true;
}