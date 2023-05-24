package cn.zhumingwu.koffer.jar;

import cn.zhumingwu.koffer.*;
import cn.zhumingwu.koffer.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 普通JAR包加解密工具类
 */
public class Koffer extends XFilters implements XConstants {

    /**
     * 加密 普通 JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param xKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, XKey xKey) throws Exception {
        encrypt(new File(src), new File(dest), xKey);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param xKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, XKey xKey) throws Exception {
        KofferEncryptor kofferEncryptor = new KofferEncryptor(new XJdkEncryptor());
        kofferEncryptor.encrypt(xKey, src, dest);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param in   原文包输入流
     * @param out  加密包输出流
     * @param xKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, XKey xKey) throws Exception {
        KofferEncryptor kofferEncryptor = new KofferEncryptor(new XJdkEncryptor());
        kofferEncryptor.encrypt(xKey, in, out);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(
            String src, String dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(new File(src), new File(dest), xKey, filter);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        KofferEncryptor kofferEncryptor = new KofferEncryptor(new XJdkEncryptor(), filter);
        kofferEncryptor.encrypt(xKey, src, dest);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param in     原文包输入流
     * @param out    加密包输出流
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(
            InputStream in, OutputStream out, XKey xKey, XEntryFilter<JarArchiveEntry> filter)
            throws Exception {
        KofferEncryptor KofferEncryptor = new KofferEncryptor(new XJdkEncryptor(), filter);
        KofferEncryptor.encrypt(xKey, in, out);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password) throws Exception {
        encrypt(src, dest, XKit.key(password));
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password) throws Exception {
        encrypt(src, dest, XKit.key(password));
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param in       原文包输入流
     * @param out      加密包输出流
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password) throws Exception {
        encrypt(in, out, XKit.key(password));
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(
            String src, String dest, String password, XEntryFilter<JarArchiveEntry> filter)
            throws Exception {
        encrypt(src, dest, XKit.key(password), filter);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(
            File src, File dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, XKit.key(password), filter);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param in       原文包输入流
     * @param out      加密包输出流
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(
            InputStream in, OutputStream out, String password, XEntryFilter<JarArchiveEntry> filter)
            throws Exception {
        encrypt(in, out, XKit.key(password), filter);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src  加密包
     * @param dest 解密包
     * @param xKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, XKey xKey) throws Exception {
        decrypt(new File(src), new File(dest), xKey);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src  加密包
     * @param dest 解密包
     * @param xKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, XKey xKey) throws Exception {
        KofferDecryptor kofferDecryptor = new KofferDecryptor(new XJdkDecryptor());
        kofferDecryptor.decrypt(xKey, src, dest);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param in   加密包输入流
     * @param out  解密包输出流
     * @param xKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, XKey xKey) throws Exception {
        KofferDecryptor KofferDecryptor = new KofferDecryptor(new XJdkDecryptor());
        KofferDecryptor.decrypt(xKey, in, out);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src    加密包
     * @param dest   解密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(
            String src, String dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(new File(src), new File(dest), xKey, filter);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src    加密包
     * @param dest   解密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        KofferDecryptor kofferDecryptor = new KofferDecryptor(new XJdkDecryptor(), filter);
        kofferDecryptor.decrypt(xKey, src, dest);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param in     加密包输入流
     * @param out    解密包输出流
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        KofferDecryptor kofferDecryptor = new KofferDecryptor(new XJdkDecryptor(), filter);
        kofferDecryptor.decrypt(xKey, in, out);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password) throws Exception {
        decrypt(src, dest, XKit.key(password));
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password) throws Exception {
        decrypt(src, dest, XKit.key(password));
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param in       加密包输入流
     * @param out      解密包输出流
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password) throws Exception {
        decrypt(in, out, XKit.key(password));
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(
            String src, String dest, String password, XEntryFilter<JarArchiveEntry> filter)
            throws Exception {
        decrypt(src, dest, XKit.key(password), filter);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(
            File src, File dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, XKit.key(password), filter);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param in       加密包输入流
     * @param out      解密包输出流
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(
            InputStream in, OutputStream out, String password, XEntryFilter<JarArchiveEntry> filter)
            throws Exception {
        decrypt(in, out, XKit.key(password), filter);
    }
}
