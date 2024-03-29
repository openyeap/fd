package cn.zhumingwu.koffer;

import cn.zhumingwu.koffer.key.XKey;

import jakarta.crypto.Cipher;
import jakarta.crypto.CipherInputStream;
import jakarta.crypto.CipherOutputStream;
import jakarta.crypto.spec.IvParameterSpec;
import jakarta.crypto.spec.SecretKeySpec;
import java.io.*;

/**
 * JDK内置加密算法的加密器
 */
public class XJdkEncryptor implements XEncryptor {

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("could not make directory: " + dest.getParentFile());
        }
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dest)) {
            encrypt(key, in, out);
        }
    }

    @Override
    public void encrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        CipherInputStream cis = null;
        try {
            String algorithm = key.getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(key.getEncryptKey(), algorithm.split("[/]")[0]),
                    new IvParameterSpec(key.getIvParameter()));
            cis = new CipherInputStream(in, cipher);
            XKit.transfer(cis, out);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            XKit.close(cis);
        }
    }

    @Override
    public InputStream encrypt(XKey key, InputStream in) throws IOException {
        try {
            String algorithm = key.getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(key.getEncryptKey(), algorithm.split("[/]")[0]),
                    new IvParameterSpec(key.getIvParameter()));
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public OutputStream encrypt(XKey key, OutputStream out) throws IOException {
        try {
            String algorithm = key.getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(key.getEncryptKey(), algorithm.split("[/]")[0]),
                    new IvParameterSpec(key.getIvParameter()));
            return new CipherOutputStream(out, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
