package cn.zhumingwu.base.util;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
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
     * 生成公私钥密钥对
     *
     * @return RSAKeyPair
     */
    public static RSAKeyPair genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
     * @return byte[]
     */


    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Key privateK = null;
        try {
            privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateK);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] decryptedData = new byte[0];
        try {
            decryptedData = cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return decryptedData;
    }

    /**
     * 私钥解密
     *
     * @param text       密文
     * @param privateKey 解密私钥
     * @return String
     */

    public static String decryptByPrivateKey(String text, String privateKey) {
        byte[] result = decryptByPrivateKey(Base64.getDecoder().decode(text), privateKey);
        return new String(result);
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return byte[]
     */

    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Key publicK = null;
        try {
            publicK = keyFactory.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, publicK);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] decryptedData = new byte[0];
        try {
            decryptedData = cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return decryptedData;
    }

    /**
     * 公钥解密
     *
     * @param text      密文
     * @param publicKey 解密公钥
     * @return String
     */

    public static String decryptByPublicKey(String text, String publicKey) {
        byte[] result = decryptByPrivateKey(Base64.getDecoder().decode(text), publicKey);
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return byte[]
     */

    public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Key publicK = null;
        try {
            publicK = keyFactory.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        // 对数据加密
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] encryptedData = new byte[0];
        try {
            encryptedData = cipher.doFinal(data);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return encryptedData;
    }

    /**
     * 公钥加密
     *
     * @param text      明文
     * @param publicKey 加密公钥
     * @return String
     */

    public static String encryptByPublicKey(String text, String publicKey) {
        byte[] result = encryptByPublicKey(Base64.getDecoder().decode(text), publicKey);
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return byte[]
     */


    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Key privateK = null;
        try {
            privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        byte[] encryptedData = new byte[0];
        try {
            encryptedData = cipher.doFinal(data);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return encryptedData;
    }

    /**
     * 私钥加密
     *
     * @param text       明文
     * @param privateKey 加密私钥
     * @return String
     */

    public static String encryptByPrivateKey(String text, String privateKey) {
        byte[] result = encryptByPrivateKey(Base64.getDecoder().decode(text), privateKey);
        return new String(result);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return String
     */

    public static String sign(byte[] data, String privateKey) {

        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        PrivateKey privateK = null;
        try {
            privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        Signature signature = null;
        try {
            signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            signature.initSign(privateK);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        try {
            signature.update(data);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        try {
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param text       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return String
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
     * @return boolean
     */

    public static boolean verify(byte[] data, String publicKeyString, String sign) {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        PublicKey publicKey = null;
        try {
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        Signature signature = null;
        try {
            signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            signature.initVerify(publicKey);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        try {
            signature.update(data);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        try {
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return boolean
     */

    public static boolean verify(String data, String publicKey, String sign) {
        return verify(Base64.getDecoder().decode(data), publicKey, sign);
    }

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
