package ltd.fdsa.database.mybatis.interceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 类字段工具类
 *
 */
public class ClassFieldsUtil {

    /**
     * 获取本类及父类的所有字段
     *
     * @param object
     * @return
     */
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
