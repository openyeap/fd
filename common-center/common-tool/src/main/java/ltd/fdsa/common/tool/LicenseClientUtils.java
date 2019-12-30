package ltd.fdsa.common.tool;

import java.util.Calendar;
 

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.common.util.LicenseUtils;

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
}