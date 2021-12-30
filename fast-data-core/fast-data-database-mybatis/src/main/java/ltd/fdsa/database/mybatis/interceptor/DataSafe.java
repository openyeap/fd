package ltd.fdsa.database.mybatis.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据安全标志
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DataSafe {

    String desc() default "此注解表示这是一个需要加密的字段，必须是字符串类型。";

}
