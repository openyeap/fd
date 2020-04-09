package ltd.fdsa.cloud.test.rsa;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.aspectj.util.FileUtil;
import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.common.util.LicenseUtils;
import ltd.fdsa.common.util.RSAUtils;  

@Slf4j
public class RSAUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {

		Map<String, Object> kp;
		try {
			kp = RSAUtils.genKeyPair();
			String privateKey = RSAUtils.getPrivateKey(kp);
			String publicKey = RSAUtils.getPublicKey(kp);

			log.info("private key: " + String.valueOf(privateKey));
			log.info("public key: " + String.valueOf(publicKey));
//			String data = "test";
			byte[] data = "this is a test text".getBytes();

			String sign = RSAUtils.sign(data, privateKey);
			log.info("sign: " + String.valueOf(sign));
			boolean verified = RSAUtils.verify(data, publicKey, sign);
			assertTrue(verified);

			byte[] encryptedData = RSAUtils.encryptByPrivateKey(data, privateKey);
			log.info("encryptedData: " + Base64.getEncoder().encodeToString(encryptedData));

			byte[] decryptedData = RSAUtils.decryptByPublicKey(encryptedData, publicKey);
			log.info("decryptedData: " + new String(decryptedData));
			assertArrayEquals(data, decryptedData);

			encryptedData = RSAUtils.encryptByPublicKey(data, publicKey);
			log.info("encryptedData: " + Base64.getEncoder().encodeToString(encryptedData));

			decryptedData = RSAUtils.decryptByPrivateKey(encryptedData, privateKey);
			log.info("decryptedData: " + new String(decryptedData));
			assertArrayEquals(data, decryptedData);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			fail(e.getLocalizedMessage());
		}
	} 

	@Test
	public void testFile() {
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		log.info(path);

		String fileName = "D:\\cert\\demo1\\demo1-public.pem";
		try {
			String publicKey = FileUtil.readAsString(new File(fileName));
			log.info(publicKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
