package cn.zhumingwu.web.utils;


import jakarta.servlet.http.HttpServletRequest;

/**
 * Shiro工具类
 *
 * 
 */
public class ShiroUtil {

    // 加密算法
    public final static String HASH_ALGORITHM_NAME = EncryptUtil.HASH_ALGORITHM_NAME;

    // 循环次数
    public final static int HASH_ITERATIONS = EncryptUtil.HASH_ITERATIONS;

    /**
     * 加密处理（64位字符）
     * 备注：采用自定义的密码加密方式，其原理与SimpleHash一致，
     * 为的是在多个模块间可以使用同一套加密方式，方便共用系统用户。
     *
     * @param password 密码
     * @param salt     密码盐
     * @return String
     */
    public static String encrypt(String password, String salt) {
        return EncryptUtil.encrypt(password, salt, HASH_ALGORITHM_NAME, HASH_ITERATIONS);
    }

    // 获取随机盐值
    public static String getRandomSalt() {
        return EncryptUtil.getRandomSalt();
    }

    // 获取用户IP地址
    public static String getIp() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        // 反向代理时获取真实ip
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
