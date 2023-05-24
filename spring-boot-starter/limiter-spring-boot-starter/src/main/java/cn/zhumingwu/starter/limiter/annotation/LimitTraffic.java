package cn.zhumingwu.starter.limiter.annotation;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface LimitTraffic {

    /**
     * initialQuantity Initial number of tokens
     *
     * @return long
     */
    long initialQuantity();

    /**
     * Maximum capacity Number defaults to initialQuantity maximumCapacity &lt; initialQuantity
     * maximumCapacity = initialQuantity
     *
     * @return long
     */
    long maximumCapacity() default 0L;

    /**
     * addedQuantity Number of tokens added at intervals
     *
     * @return long
     */
    long addedQuantity();

    /**
     * Additional capacity interval time When using, please cooperate with timeUnit
     *
     * @return long
     */
    long intervalTime() default 1000L;

    /**
     * Unit of time
     *
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
