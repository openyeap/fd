package ltd.fdsa.maven.codegg.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface isUnique {
    boolean value() default true;

}
