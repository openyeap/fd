package ltd.fdsa.common.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Map;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.common.util.LicenseUtils;
import ltd.fdsa.common.util.RSAUtils;

/**
 * java获取机器码，注册码的实现
 * 
 */

@Slf4j
public class LicenseClientUtils {

	@SneakyThrows
	public static String generateSerialNumber(String machineCode, String privateKey, int hours) {
		machineCode = machineCode.replace("-", "");
		// 得到过期时间
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.HOUR, hours);
		long expried = rightNow.getTimeInMillis() * 1000;

		String code = String.format("%X", expried);
		while (code.length() < 16) {
			code = "0" + code;
		}
		String body = code;
		// 与机器码一起生成签名
		String checkSum = LicenseUtils.CheckSum(body + machineCode, privateKey, "");
		return body + checkSum;
	}

	@SneakyThrows
	public static boolean generatePKI(String root) {
		Map<String, Object> kp = RSAUtils.genKeyPair();
		String privateKey = RSAUtils.getPrivateKey(kp);
		String publicKey = RSAUtils.getPublicKey(kp);

		log.info("private key: " + String.valueOf(privateKey));
		log.info("public key: " + String.valueOf(publicKey));
		// Get the file reference
		Path path = Paths.get(root + "/public.pem");
 		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(publicKey);
		}
		path = Paths.get(root + "/private.pem");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(publicKey);
		}

		return true;
	}
}