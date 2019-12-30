package ltd.fdsa.common.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.util.StringUtils;

public class Application {

	public static void main(String[] args) {

		String machineCode = getMachineCode();
		String projectPath = getPrivateKey();
		projectPath = System.getProperty("user.dir") + "/" + projectPath;
		String privateKey = readFile(projectPath);
		int hours = getExpriedTime();
		LicenseClientUtils.generateSerialNumber(machineCode, privateKey, hours);

	}

	private static String readFile(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long fileLength = file.length();
		byte[] fileContent = new byte[fileLength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(fileContent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(fileContent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

	public static String getMachineCode() {
		try (Scanner scanner = new Scanner(System.in);) {
			StringBuilder help = new StringBuilder();
			help.append("请输入机器码：");
			System.out.println(help.toString());
			if (scanner.hasNext()) {
				String ipt = scanner.next();
				if (StringUtils.isEmpty(ipt)) {
					return ipt;
				}
			}
			return "test";
		}
	}

	public static String getPrivateKey() {
		try (Scanner scanner = new Scanner(System.in);) {
			StringBuilder help = new StringBuilder();
			help.append("请输入私钥文件名：");
			System.out.println(help.toString());
			if (scanner.hasNext()) {
				String ipt = scanner.next();
				if (StringUtils.isEmpty(ipt)) {
					return ipt;
				}
			}
			return "test";
		}
	}

	public static int getExpriedTime() {
		try (Scanner scanner = new Scanner(System.in);) {
			StringBuilder help = new StringBuilder();
			help.append("请输入过期时间：");
			System.out.println(help.toString());
			if (scanner.hasNext()) {
				String ipt = scanner.next();
				if (StringUtils.isEmpty(ipt)) {
					return Integer.parseInt(ipt);
				}
			}
			return 1;
		}
	}

}