package ltd.fdsa.core.util;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtils {

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 加密
     */
    private static final int KEY_SIZE = 2048;

    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";


    /**
     * 生成公私钥密钥对
     *
     * @return
     */
    @SneakyThrows
    public static RSAKeyPair genKeyPair() {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        RSAKeyPair rsaKeyPair = new RSAKeyPair(publicKeyString, privateKeyString);
        return rsaKeyPair;
    }


    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return @
     */
    @SneakyThrows
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return decryptedData;
    }

    /**
     * 私钥解密
     *
     * @param text       密文
     * @param privateKey 解密私钥
     * @return
     */
    @SneakyThrows
    public static String decryptByPrivateKey(String text, String privateKey) {
        byte[] result = decryptByPrivateKey(Base64.getDecoder().decode(text), privateKey);
        return new String(result);
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return @
     */
    @SneakyThrows
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return decryptedData;
    }


    /**
     * 公钥解密
     *
     * @param text      密文
     * @param publicKey 解密公钥
     * @return
     */
    @SneakyThrows
    public static String decryptByPublicKey(String text, String publicKey) {
        byte[] result = decryptByPrivateKey(Base64.getDecoder().decode(text), publicKey);
        return new String(result);
    }


    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return @
     */
    @SneakyThrows
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData;
    }

    /**
     * 公钥加密
     *
     * @param text      明文
     * @param publicKey 加密公钥
     * @return
     */
    @SneakyThrows
    public static String encryptByPublicKey(String text, String publicKey) {
        byte[] result = encryptByPublicKey(Base64.getDecoder().decode(text), publicKey);
        return new String(result);
    }


    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return @
     */
    @SneakyThrows
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);

        byte[] encryptedData = cipher.doFinal(data);

        return encryptedData;
    }

    /**
     * 私钥加密
     *
     * @param text       明文
     * @param privateKey 加密私钥
     * @return
     */
    @SneakyThrows
    public static String encryptByPrivateKey(String text, String privateKey) {
        byte[] result = encryptByPrivateKey(Base64.getDecoder().decode(text), privateKey);
        return new String(result);
    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return @
     */
    @SneakyThrows
    public static String sign(byte[] data, String privateKey) {

        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param text       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return @
     */
    public static String sign(String text, String privateKey) {
        return sign(Base64.getDecoder().decode(text), privateKey);
    }


    /**
     * 校验数字签名
     *
     * @param data            已加密数据
     * @param publicKeyString 公钥(BASE64编码)
     * @param sign            数字签名
     * @return @
     */
    @SneakyThrows
    public static boolean verify(byte[] data, String publicKeyString, String sign) {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
    }


    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return @
     */
    @SneakyThrows
    public static boolean verify(String data, String publicKey, String sign) {
        return verify(Base64.getDecoder().decode(data), publicKey, sign);
    }

//    @SneakyThrows
//    public static PrivateKey getPrivateKey(String privateKeyString) {
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
//        return privateKey;
//    }
//
//    @SneakyThrows
//    public static PublicKey getPublicKey(String publicKeyString) {
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        PublicKey publicKey = keyFactory.generatePublic(keySpec);
//        return publicKey;
//    }

    public static class RSAKeyPair {
        private final String publicKey;
        private final String privateKey;

        public RSAKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }
    }

}
