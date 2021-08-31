package ltd.fdsa.maven.codegg;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

    /**
     * 默认使用的lock名称
     *
     * @return
     */
    String name() default "";

}
