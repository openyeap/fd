package cn.zhumingwu.database.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Properties;

/**
 * 加密参数值插件
 *
 */
@Intercepts({
        @Signature(
                type = ParameterHandler.class,
                method = "setParameters",
                args = {PreparedStatement.class}
        )
})
@Slf4j
public class EncryptParameterInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) {
        Object returnObject = null;
        try {
            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
            Object obj = parameterHandler.getParameterObject();

            if (obj != null) {
                List<Field> fields = ClassFieldsUtil.getAllFields(obj);
                for (Field field : fields) {
                    // 设置对象的访问权限，保证对private的属性的访问
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(DataSafe.class)) {
                        if (field.getType().getName() instanceof String) {
                            String originalValue = (String) field.get(obj);
                            if (originalValue != null) {
                                String result = DataDigest.encrypt(originalValue);
                                field.set(obj, result);
                                log.debug("参数字段加密：{}#{}", obj.getClass().getName(), field.getName());
                            }
                        } else {
                            throw new Exception("加密字段：" + obj.getClass().getName() + " : " + field.getName() + " 必须是String类型。");
                        }
                    }
                }
            }
            // 执行数据库操作
            returnObject = invocation.proceed();
        } catch (Exception e) {
            log.error("加密参数值插件异常", e);
        }
        return returnObject;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ParameterHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
