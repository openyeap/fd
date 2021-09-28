package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default "";

    String name() default "";

    String remark() default "";

    boolean isNull() default true;

    boolean autoIncrement() default false;

    String type() default "";

    int length() default 0;

    int scale() default 0;
}
