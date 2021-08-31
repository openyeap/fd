package ltd.fdsa.maven.codegg;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface isUnique {
    boolean isUnique() default true;

}
