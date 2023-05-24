package cn.zhumingwu.database.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.util.AESUtils;

/**
 * 数据加密解密
 *
 */
@Slf4j
public class DataDigest {

    /**
     * 默认秘钥，16位
     */
    private static final String KEY = "vF1Y7GFn4Ne1Ah0x";

    /**
     * 加密
     *
     * @param source 原始字符
     * @return 加密之后的字符
     */
    public static String encrypt(String source) {
        if (source == null) {
            return null;
        }
        try {
            String encrypt = AESUtils.encrypt(source, KEY);
            return encrypt;
        } catch (Exception e) {
            log.warn("加密数据失败：" + source);
            return source;
        }
    }

    /**
     * 解密
     *
     * @param source 需要解密内容
     * @return 解密结果
     */
    public static String decrypt(String source) {
        if (source == null) {
            return null;
        }
        try {
            String encrypt = AESUtils.decrypt(source, KEY);
            return encrypt;
        } catch (Exception e) {
            log.warn("解密数据失败：" + source);
            return source;
        }
    }
}
