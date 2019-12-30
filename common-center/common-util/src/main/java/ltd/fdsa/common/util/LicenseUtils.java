package ltd.fdsa.common.util;

import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;

import com.google.common.base.Strings;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
 

/**
 * java获取机器码，注册码的实现
 * 
 */

//@Slf4j
public class LicenseUtils {
	private static final int SPLITLENGTH = 4;

	public static String getMachineCode(String salt)   {
		StringBuffer result = new StringBuffer();
		String mac = getMac();
		result.append("mac: ");
		result.append(mac);
		
		Properties props = System.getProperties();
		String javaVersion = props.getProperty("java.version");
		result.append("java.version: ");
		result.append(javaVersion);
		String javaVMVersion = props.getProperty("java.vm.version");
		result.append("jvm.version: ");
		result.append(javaVMVersion);
		String osVersion = props.getProperty("os.version");
		result.append("os.version: ");
		result.append(osVersion);
		
		result.append("salt: ");
		result.append(salt);
//		String code = RSAUtils.sign(result.toString().getBytes(), privateKey);
		String code = CheckSum(result.toString());
		return getSplitString(code, "-", SPLITLENGTH);
	}
	@SneakyThrows
	public static boolean verifySerialNumber(String publicKey, String serialNumber,String salt)  {

		String machineCode = getMachineCode(salt).replace("-", "");
		String body = serialNumber.substring(0, 16);
		String sign = serialNumber.substring(16);
//		log.info("sign ：" +sign);

		if (!sign.equals(CheckSum(body + machineCode, sign, publicKey))) {
			return false;
		}
		Long expried = Long.valueOf(body, 16);

		Calendar rightNow = Calendar.getInstance();

		if (rightNow.getTimeInMillis() * 1000 > expried) {
			return false;
		}
		return true;
	}


	public static String CheckSum(String input, String privateKey, String publicKey)  {
//		log.info("data ：" + input);

		if (!Strings.isNullOrEmpty(publicKey)) {
//			log.info("sign ：" + privateKey);

			if (RSAUtils.verify(input, publicKey, privateKey)) {
				return privateKey;
			}
			return input;
		}
		return RSAUtils.sign(input, privateKey);

	}

	public static String CheckSum(String input) {
		Long h = 0l;
		for (char item : input.toCharArray()) {
			// h = 31 * h + item;
			h = (h << 5) - h + item;
		}

		String code = String.format("%X", h);
		while (code.length() < 16) {
			code = "0" + code;
		}
		return code;
	}

	private static String getSplitString(String str, String split, int length) {
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < len; i++) {
			if (i % length == 0 && i > 0) {
				temp.append(split);
			}
			temp.append(str.charAt(i));
		}
		String[] attrs = temp.toString().split(split);
		StringBuilder finalMachineCode = new StringBuilder();
		for (String attr : attrs) {
			if (attr.length() == length) {
				finalMachineCode.append(attr).append(split);
			}
		}
		String result = finalMachineCode.toString().substring(0, finalMachineCode.toString().length() - 1);
		return result;
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	private static String getMac() {
		try {
			Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				byte[] mac = el.nextElement().getHardwareAddress();
				if (mac == null)
					continue;

				String hexstr = bytesToHexString(mac);
				return hexstr;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
}