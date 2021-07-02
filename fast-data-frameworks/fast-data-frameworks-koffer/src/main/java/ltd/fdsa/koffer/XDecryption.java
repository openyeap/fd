package ltd.fdsa.koffer;

import ltd.fdsa.koffer.filter.XAllEntryFilter;
import ltd.fdsa.koffer.filter.XAnyEntryFilter;
import ltd.fdsa.koffer.filter.XMixEntryFilter;
import ltd.fdsa.koffer.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import static ltd.fdsa.koffer.XFilters.*;

/**
 * 解密
 */
public class XDecryption {
    private File Koffer;
    private XKey key;
    private XAnyEntryFilter<JarArchiveEntry> includes = XKit.any();
    private XAllEntryFilter<JarArchiveEntry> excludes = XKit.all();

    /**
     * 指定密文包路径
     *
     * @param Koffer 密文包路径
     * @return {@code this}
     */
    public XDecryption from(String Koffer) {
        return from(new File(Koffer));
    }

    /**
     * 指定密文包文件
     *
     * @param Koffer 密文包文件
     * @return {@code this}
     */
    public XDecryption from(File Koffer) {
        this.Koffer = Koffer;
        return this;
    }

    /**
     * 指定密码
     *
     * @param password 密码
     * @return {@code this}
     */
    public XDecryption use(String password) {
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
     * @param password  解密密码
     * @return {@code this}
     */
    public XDecryption use(String algorithm, int keysize, int ivsize, String password) {
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
    public XDecryption include(String ant) {
        includes.mix(ant(ant));
        return this;
    }

    /**
     * 指定包含资源的正则表达式, 可指定多个.
     *
     * @param regex 包含资源的正则表达式
     * @return {@code this}
     */
    public XDecryption include(Pattern regex) {
        includes.mix(regex(regex.pattern()));
        return this;
    }

    /**
     * 指定排除资源的ANT表达式, 可指定多个.
     *
     * @param ant 排除资源的ANT表达式
     * @return {@code this}
     */
    public XDecryption exclude(String ant) {
        excludes.mix(not(ant(ant)));
        return this;
    }

    /**
     * 指定排除资源的正则表达式, 可指定多个.
     *
     * @param regex 排除资源的正则表达式
     * @return {@code this}
     */
    public XDecryption exclude(Pattern regex) {
        excludes.mix(not(regex(regex.pattern())));
        return this;
    }

    /**
     * 指定原文包路径, 并执行解密.
     *
     * @param jar 原文包路径
     * @throws Exception 解密异常
     */
    public void to(String jar) throws Exception {
        to(new File(jar));
    }

    /**
     * 指定原文包文件, 并执行解密.
     *
     * @param jar 原文包文件
     * @throws Exception 解密异常
     */
    public void to(File jar) throws Exception {
        if (Koffer == null) {
            throw new IllegalArgumentException(
                    "Koffer to decrypt is null. [please call from(String Koffer) or from(File Koffer) before]");
        }
        if (key == null) {
            throw new IllegalArgumentException(
                    "key to decrypt is null. [please call use(String password) or use(String algorithm, int keysize, int ivsize, String password) before]");
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
        XCryptos.decrypt(Koffer, jar, key, filter);
    }
}
