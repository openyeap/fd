package cn.zhumingwu.database.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Statement;
import java.util.*;

@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
@Slf4j
public class DecryptResultFieldInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        Object returnValue = null;
        try {
            // 执行查询操作
            returnValue = invocation.proceed();
            if (returnValue != null) {
                // 对结果进行处理
                try {
                    if (returnValue instanceof ArrayList<?>) {
                        // 查询结果是List或单个对象
                        List<?> list = (ArrayList<?>) returnValue;
                        long startTime = System.currentTimeMillis();
                        for (Object obj : list) {
                            handlerFields(obj);
                        }
                        log.debug("自动解密耗时：{}{}", (System.currentTimeMillis() - startTime), "ms");
                    }
                } catch (Exception e) {
                    log.error("自动解密异常", e);
                }
            }
        } catch (Exception e) {
            log.error("解密字段值插件异常", e);
        }
        return returnValue;
    }

    /**
     * 字段处理，最终实现解密
     *
     * @param obj
     * @throws Exception
     */
    private void handlerFields(Object obj) throws Exception {
        List<Field> fields = ClassFieldsUtil.getAllFields(obj);
        for (Field field : fields) {
            handlerChildFields(obj, field);
        }
    }

    /**
     * 处理子类字段，最终实现解密
     *
     * @param obj
     * @param field
     * @throws Exception
     */
    private void handlerChildFields(Object obj, Field field) throws Exception {
        // 设置对象的访问权限，保证对private的属性的访问
        field.setAccessible(true);
        if (Collection.class.isAssignableFrom(field.getType())) {
            // 字段类型为List 或 Set集合
            Collection<?> childCollection;
            Type type = field.getGenericType();
            // 判断是不是参数化类型
            if (type instanceof ParameterizedType) {
                // 集合的参数类型
                Class<?> clazz = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                childCollection = setElementValue(obj, field, clazz, field.getType());
                field.set(obj, childCollection);
            }
        } else {
            decryptField(obj, field);
        }
    }

    /**
     * 修改集合元素值
     *
     * @param obj
     * @param field
     * @param elementType
     * @param collectionType
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> Collection<T> setElementValue(Object obj, Field field, T elementType, Class<?> collectionType)
            throws Exception {
        Collection<T> collection;
        if (collectionType == List.class) {
            collection = new ArrayList<T>();
        } else {
            collection = new HashSet<T>();
        }
        Collection<T> originalElements = (Collection<T>) field.get(obj);
        if (originalElements == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(originalElements)) {
            return null;
        }
        for (T innerObject : originalElements) {
            // 集合元素中的字段
            List<Field> innerFields = ClassFieldsUtil.getAllFields(innerObject);
            for (Field innerField : innerFields) {
                innerField.setAccessible(true);
                // 递归
                handlerChildFields(innerObject, innerField);
            }
            collection.add(innerObject);
        }
        return collection;
    }

    private void decryptField(Object obj, Field field) throws Exception {
        if (field.isAnnotationPresent(DataSafe.class)) {
            if (!(field.getType().getName() instanceof String)) {
                throw new Exception("解密字段：" + obj.getClass().getName() + " : " + field.getName() + " 必须是String类型。");
            }
            String originalValue = (String) field.get(obj);
            if (originalValue != null) {
                String result = DataDigest.decrypt(originalValue);
                field.set(obj, result);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
