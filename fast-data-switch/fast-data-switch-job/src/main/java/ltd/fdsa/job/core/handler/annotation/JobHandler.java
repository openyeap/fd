package ltd.fdsa.job.core.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation for job handler
 *
 * will be replaced by {@link ltd.fdsa.job.core.handler.annotation.Job}
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Deprecated
public @interface JobHandler {

    String value();

}
