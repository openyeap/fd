package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UI {
    String value() default "";

    String defaultValues() default "";

    String tips() default "";

    String holdPlace() default "";

    String validation() default "";

    String message() default "";

    Show[] show() default Show.Default;

    boolean readonly() default false;

    int length() default 0;

    public enum Show {
        Default,
        Create, Update, List, Query,
    }
}
