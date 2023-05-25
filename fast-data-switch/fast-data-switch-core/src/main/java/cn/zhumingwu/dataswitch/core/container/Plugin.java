package cn.zhumingwu.dataswitch.core.container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 用于插件的描述
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Plugin {
    String name() default "";

    PluginType type() default PluginType.PIPELINE;

    String description() default "";
}
