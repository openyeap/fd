package ltd.fdsa.koffer.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class KReflection {

    public static XField field(Class<?> clazz, String name) throws NoSuchFieldException {
        if (clazz == null) {
            throw new NullPointerException();
        }
        if (name == null) {
            throw new IllegalArgumentException("field name == null");
        }
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return new XField(field);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(name);
    }

    public static XMethod method(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        if (clazz == null) {
            throw new NullPointerException();
        }
        if (name == null) {
            throw new IllegalArgumentException("method name == null");
        }
        while (clazz != null) {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return new XMethod(method);
            } finally {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchMethodException(name);
    }
}
