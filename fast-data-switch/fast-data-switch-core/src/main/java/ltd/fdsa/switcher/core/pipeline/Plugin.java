package ltd.fdsa.switcher.core.pipeline;

import ltd.fdsa.switcher.core.PluginType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//public interface Plugin {
//    String getName();
//
//    PluginType getType();
//
//    String getDescription();
//}
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Plugin {

    /**
     * 默认使用的lock名称
     *
     * @return
     */
    String name() default "";

    PluginType type() default PluginType.PIPELINE;

    String description() default "";
}
