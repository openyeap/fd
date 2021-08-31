package ltd.fdsa.maven.codegg;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Scale {


    int scale() default 0;

}
