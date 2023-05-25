package cn.zhumingwu.dataswitch.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TypeConvertUtils {

    /**
     * 数据类型转换
     *
     * @param src
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object src, Class<T> clazz) {
        if (src == null) {
            return null;
        } else if (src instanceof String) {
            if (clazz == Integer.class) {
                return (T) Integer.valueOf(src.toString());
            } else if (clazz == Long.class) {
                return (T) Long.valueOf(src.toString());
            } else if (clazz == Double.class) {
                return (T) Double.valueOf(src.toString());
            } else if (clazz == Float.class) {
                return (T) Float.valueOf(src.toString());
            } else if (clazz == Boolean.class) {
                return (T) Boolean.valueOf(src.toString());
            } else if (clazz == Short.class) {
                return (T) Short.valueOf(src.toString());
            } else if (clazz == Byte.class) {
                return (T) Byte.valueOf(src.toString());
            } else if (clazz == BigInteger.class) {
                return (T) BigInteger.valueOf(Long.valueOf(src.toString()));
            } else if (clazz == BigDecimal.class) {
                return (T) new BigDecimal(src.toString());
            }
        } else if (clazz == String.class) {
            return (T) src.toString();
        }
        return (T) src;
    }
}
