package ltd.fdsa.cloud.test.rsa;

import static org.junit.Assert.*;

import java.util.Base64;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j; 
import ltd.fdsa.cloud.util.LicenseUtils;
import ltd.fdsa.cloud.util.RSAUtils;

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
	public void testM() {

		Map<String, Object> kp;
		try {
			kp = RSAUtils.genKeyPair();
			String privateKey = RSAUtils.getPrivateKey(kp);
			String publicKey = RSAUtils.getPublicKey(kp);
			String machineCode = LicenseUtils.getMachineCode();
			log.info("MachineCode: " + machineCode);

			String serialNumber = LicenseUtils.generateSerialNumber( machineCode, privateKey, 1);
			log.info("SerialNumber: " + serialNumber);
			boolean result = LicenseUtils.verifySerialNumber(publicKey, serialNumber);
			log.info("result: " + String.valueOf(result));
			assertTrue(result);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			fail(e.getLocalizedMessage());
		}

	}

}
