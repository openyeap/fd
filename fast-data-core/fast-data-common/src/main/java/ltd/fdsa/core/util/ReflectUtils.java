package ltd.fdsa.core.util;

import org.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ReflectUtils {

    /**
     * 获取对象对应泛型的类型参数。
     *
     * @param <T>     class
     * @param obj     对象
     * @param rawType 泛型的原始类型
     * @return 泛型定义的第一个类型参数
     */
    public static <T> Class<T> getTypeArgClass(Object obj, Class<?> rawType) {
        return getTypeArgClass(ClassUtils.getUserClass(obj), rawType);
    }

    /**
     * 获取类对应泛型的类型参数。
     *
     * @param <T>     class
     * @param clazz   类
     * @param rawType 泛型的原始类型
     * @return 泛型定义的第一个类型参数
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getTypeArgClass(Class<?> clazz, Class<?> rawType) {
        if (rawType.isInterface()) {
            while (clazz != Object.class) {
                for (Type type : clazz.getGenericInterfaces()) {
                    if (type instanceof ParameterizedType) {
                        ParameterizedType p = (ParameterizedType) type;
                        if (p.getRawType() == rawType) {
                            return (Class<T>) p.getActualTypeArguments()[0];
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
        } else {
            Type parentType = clazz.getGenericSuperclass();
            while (parentType != null) {
                if (parentType instanceof Class<?>) {
                    parentType = ((Class<?>) parentType).getGenericSuperclass();
                } else {
                    ParameterizedType p = (ParameterizedType) parentType;
                    if (p.getRawType() == rawType) {
                        return (Class<T>) p.getActualTypeArguments()[0];
                    }
                    parentType = ((Class<?>) p.getRawType()).getGenericSuperclass();
                }
            }
        }
        return null;
    }
}
