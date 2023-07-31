package cn.zhumingwu.starter.jdbc.mappers;//package cn.zhumingwu.starter.jdbc.mappers;


import cn.zhumingwu.starter.jdbc.pojo.DefaultDyno;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class TestMapper<T> {

    private Class<T> clazz;


    private TestMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> TestMapper<T> build(Class<T> clazz) {
        return new TestMapper<T>(clazz);
    }


    public T getEntity(Map<String, Object> data) {

        try {

            T o = this.clazz.newInstance();
            Class<Annotation> annotationClass = (Class<Annotation>) ClassUtils.forName("cn.zhumingwu.starter.jdbc.MyA", null);

            var annotation = this.clazz.getAnnotation(annotationClass);
            var annotationType = annotation.annotationType();
            var name1 = annotationType.getMethod("name");
            var valsue = name1.invoke(annotation);
            for (var f : this.clazz.getDeclaredFields()) {
                var name = f.getName();
                var value = data.get(name);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                f.set(o, value);
            }
            var sClazz = this.clazz.getSuperclass();
            for (var f : sClazz.getDeclaredFields()) {
                var name = f.getName();
                var value = data.get(name);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                f.set(o, value);
            }
            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T getDyno(Map<String, Object> data) {
        var o = new DefaultDyno(data);
        return (T) o;
    }
}