package ltd.fdsa.koffer;

import ltd.fdsa.koffer.key.XKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 解密器
 */
public interface XDecryptor {

    /**
     * 解密，将目标文件解密输出至目标文件。
     *
     * @param key  密钥
     * @param src  源文件
     * @param dest 目标文件
     * @throws IOException I/O 异常
     */
    void decrypt(XKey key, File src, File dest) throws IOException;

    /**
     * 解密，将输入流解密输出至输出流。
     *
     * @param key 密钥
     * @param in  输入流
     * @param out 输出流
     * @throws IOException I/O 异常
     */
    void decrypt(XKey key, InputStream in, OutputStream out) throws IOException;

    /**
     * 解密，将输入流包装成解密的输入流
     *
     * @param key 密钥
     * @param in  输入流
     * @return 解密后的输入流
     * @throws IOException I/O 异常
     */
    InputStream decrypt(XKey key, InputStream in) throws IOException;

    /**
     * 解密，将输入流包装成解密的输出流
     *
     * @param key 密钥
     * @param out 输出流
     * @return 解密后的输出流
     * @throws IOException I/O 异常
     */
    OutputStream decrypt(XKey key, OutputStream out) throws IOException;
}
