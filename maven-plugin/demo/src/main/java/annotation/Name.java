package ltd.fdsa.maven.codegg.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

    /**
     * 默认使用的lock名称
     *
     * @return
     */
    String value() default "";

}
