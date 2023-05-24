package cn.zhumingwu.koffer;

import cn.zhumingwu.koffer.filter.XAllEntryFilter;
import cn.zhumingwu.koffer.filter.XAnyEntryFilter;
import cn.zhumingwu.koffer.filter.XMixEntryFilter;
import cn.zhumingwu.koffer.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import static cn.zhumingwu.koffer.XFilters.*;

/**
 * 加密
 */
public class XEncryption {
    private File jar;
    private XKey key;
    private XAnyEntryFilter<JarArchiveEntry> includes = XKit.any();
    private XAllEntryFilter<JarArchiveEntry> excludes = XKit.all();

    /**
     * 指定原文包路径
     *
     * @param jar 原文包路径
     * @return {@code this}
     */
    public XEncryption from(String jar) {
        return from(new File(jar));
    }

    /**
     * 指定原文包文件
     *
     * @param jar 原文包文件
     * @return {@code this}
     */
    public XEncryption from(File jar) {
        this.jar = jar;
        return this;
    }

    /**
     * 指定密码
     *
     * @param password 密码
     * @return {@code this}
     */
    public XEncryption use(String password) {
        try {
            this.key = XKit.key(password);
            return this;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 指定高级密码
     *
     * @param algorithm 算法名称
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param password  加密密码
     * @return {@code this}
     */
    public XEncryption use(String algorithm, int keysize, int ivsize, String password) {
        try {
            this.key = XKit.key(algorithm, keysize, ivsize, password);
            return this;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 指定包含资源的ANT表达式, 可指定多个.
     *
     * @param ant 包含资源的ANT表达式
     * @return {@code this}
     */
    public XEncryption include(String ant) {
        includes.mix(ant(ant));
        return this;
    }

    /**
     * 指定包含资源的正则表达式, 可指定多个.
     *
     * @param regex 包含资源的正则表达式
     * @return {@code this}
     */
    public XEncryption include(Pattern regex) {
        includes.mix(regex(regex.pattern()));
        return this;
    }

    /**
     * 指定排除资源的ANT表达式, 可指定多个.
     *
     * @param ant 排除资源的ANT表达式
     * @return {@code this}
     */
    public XEncryption exclude(String ant) {
        excludes.mix(not(ant(ant)));
        return this;
    }

    /**
     * 指定排除资源的正则表达式, 可指定多个.
     *
     * @param regex 排除资源的正则表达式
     * @return {@code this}
     */
    public XEncryption exclude(Pattern regex) {
        excludes.mix(not(regex(regex.pattern())));
        return this;
    }

    /**
     * 指定密文包路径, 并执行加密.
     *
     * @param Koffer 密文包路径
     * @throws Exception 加密异常
     */
    public void to(String Koffer) throws Exception {
        to(new File(Koffer));
    }

    /**
     * 指定密文包文件, 并执行加密.
     *
     * @param Koffer 密文包文件
     * @throws Exception 加密异常
     */
    public void to(File Koffer) throws Exception {
        if (jar == null) {
            throw new IllegalArgumentException(
                    "jar to encrypt is null. [please call from(String jar) or from(File jar) before]");
        }
        if (key == null) {
            throw new IllegalArgumentException(
                    "key to encrypt is null. [please call use(String password) or use(String algorithm, int keysize, int ivsize, String password) before]");
        }
        XMixEntryFilter<JarArchiveEntry> filter;
        if (includes.size() == 0 && excludes.size() == 0) {
            filter = null;
        } else {
            filter = XKit.all();
            if (includes.size() > 0) {
                filter.mix(includes);
            }
            if (excludes.size() > 0) {
                filter.mix(excludes);
            }
        }
        XCryptos.encrypt(jar, Koffer, key, filter);
    }
}
