package cn.zhumingwu.base.util;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES加密
 *
 */
public class AESUtils {

    private static final String CIPHER_STRING = "AES/ECB/PKCS5Padding";
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest sha;
        key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
    }

    public static String encrypt(String strToEncrypt, String secret) throws Exception {
        setKey(secret);
        Cipher cipher = Cipher.getInstance(CIPHER_STRING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    public static String decrypt(String strToDecrypt, String secret) throws Exception {
        setKey(secret);
        Cipher cipher = Cipher.getInstance(CIPHER_STRING);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }
}