package cn.zhumingwu.database.mybatis.interceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 类字段工具类
 */
public class ClassFieldsUtil {

    public static List<Field> getAllFields(Object object) {
        if (object == null) {
            return Collections.emptyList();
        }
        List<Field> fieldList = new ArrayList<>();
        Class currentClass = object.getClass();
        while (currentClass != null) {
            fieldList.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return fieldList;
    }
}
