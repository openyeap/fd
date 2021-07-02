package ltd.fdsa.starter.remote.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 修饰：域、参数、方法
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
/**
 * 作用是定义被它所注解的注解保留多久，一共有三种策略，定义在RetentionPolicy枚举中
 * 1. SOURCE：被编译器忽略
 * 2. CLASS：注解将会被保留在Class文件中，但在运行时并不会被VM保留。这是默认行为，所有没有用Retention注解的注解，都会采用这种策略
 * 3. RUNTIME：保留至运行时。所以我们可以通过反射去获取注解信息。
 */
@Retention(RetentionPolicy.RUNTIME)
/**
 * 限定注解
 */
@Qualifier
public @interface Distributed {

}