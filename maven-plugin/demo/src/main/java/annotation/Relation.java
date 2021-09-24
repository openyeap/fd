package annotation;

import model.RelationDefine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {
    Class entity() default void.class;

    String field() default "id";

    RelationDefine.Type value() default RelationDefine.Type.One2One;
}
