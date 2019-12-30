package ltd.fdsa.common.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import lombok.SneakyThrows;

public class RSAUtils {

	/** */
	/**
	 * 加密算法RSA
	 */
	private static final String KEY_ALGORITHM = "RSA";

	/** */
	/**
	 * 签名算法
	 */
	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

	/** */
	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/** */
	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/** */
	/**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 * 
	 * @return
	 * @
	 */

	@SneakyThrows
	public static Map<String, Object> genKeyPair()  {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(2048);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/** */
	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 * 
	 * @param data       已加密数据
	 * @param privateKey 私钥(BASE64编码)
	 * 
	 * @return
	 * @
	 */
	@SneakyThrows
	public static String sign(byte[] data, String privateKey)  {

		byte[] keyBytes = Base64.getDecoder().decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return Base64.getEncoder().encodeToString(signature.sign());
	}

	/** */
	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 * 
	 * @param data       已加密数据
	 * @param privateKey 私钥(BASE64编码)
	 * 
	 * @return
	 * @
	 */
	public static String sign(String data, String privateKey)  {
		return sign(Base64.getDecoder().decode(data), privateKey);
	}

	/** */
	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 * 
	 * @param data      已加密数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign      数字签名
	 * 
	 * @return
	 * @
	 * 
	 */
	@SneakyThrows
	public static boolean verify(byte[] data, String publicKey, String sign)  {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(Base64.getDecoder().decode(sign));
	}

	/** */
	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 * 
	 * @param data      已加密数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign      数字签名
	 * 
	 * @return
	 * @
	 * 
	 */

	@SneakyThrows
	public static boolean verify(String data, String publicKey, String sign)   {
		return verify(Base64.getDecoder().decode(data), publicKey, sign);
	}

	/** */
	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 * 
	 * @param encryptedData 已加密数据
	 * @param privateKey    私钥(BASE64编码)
	 * @return
	 * @
	 */
	@SneakyThrows
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)  {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		byte[] decryptedData = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 * 
	 * @param encryptedData 已加密数据
	 * @param publicKey     公钥(BASE64编码)
	 * @return
	 * @
	 */
	@SneakyThrows
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)  {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		byte[] decryptedData = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 * 
	 * @param data      源数据
	 * @param publicKey 公钥(BASE64编码)
	 * @return
	 * @
	 */

	@SneakyThrows
	public static byte[] encryptByPublicKey(byte[] data, String publicKey)   {
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

	/** */
	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data       源数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @
	 */

	@SneakyThrows
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey)  {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);

		byte[] encryptedData = cipher.doFinal(data);

		return encryptedData;
	}

	/** */
	/**
	 * <p>
	 * 获取私钥
	 * </p>
	 * 
	 * @param keyMap 密钥对
	 * @return
	 * @
	 */
	public static String getPrivateKey(Map<String, Object> keyMap)  {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	/** */
	/**
	 * <p>
	 * 获取公钥
	 * </p>
	 * 
	 * @param keyMap 密钥对
	 * @return
	 * @
	 */
	public static String getPublicKey(Map<String, Object> keyMap)  {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

}