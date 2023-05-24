package cn.zhumingwu.starter.limiter.core.utils;

import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

public class AnnotatedMethodsUtils {
    private AnnotatedMethodsUtils() {
    }

    public static <T extends Annotation> Map<Method, T> getMethodAndAnnotation(
            Object bean, Class<T> annotation) {
        return MethodIntrospector.selectMethods(
                bean.getClass(),
                (MethodIntrospector.MetadataLookup<T>)
                        method -> AnnotatedElementUtils.findMergedAnnotation(method, annotation));
    }
}
