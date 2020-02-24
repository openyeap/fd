package ltd.fdsa.common.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.util.StringUtils;

public class Application {

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			String machineCode = getMachineCode(scanner);
			String projectPath = getPrivateKey(scanner);
			projectPath = System.getProperty("user.dir") + "/" + projectPath;
			String file = projectPath + "/public.pem";
			if (!Files.exists(Paths.get(file))) {
				LicenseClientUtils.generatePKI(projectPath);
			}
			String privateKey = readFile(file);
			int hours = getExpriedTime(scanner);
			LicenseClientUtils.generateSerialNumber(machineCode, privateKey, hours);

		}
	}

	private static String readFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return "";
		}
		String encoding = "UTF-8";

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

	public static String getMachineCode(Scanner scanner) {

		StringBuilder help = new StringBuilder();
		help.append("请输入机器码：");
		while (true) {
			System.out.println(help.toString());
			String input = scanner.next();
			if (!StringUtils.isEmpty(input)) {
				System.out.println(input);
				return input;
			}
		}
	}

	public static String getPrivateKey(Scanner scanner) {

		StringBuilder help = new StringBuilder();
		help.append("请输入私钥文件名：");
		while (true) {
			System.out.println(help.toString());
			String input = scanner.next();
			if (!StringUtils.isEmpty(input)) {
				System.out.println(input);
				return input;
			}
		}

	}

	public static int getExpriedTime(Scanner scanner) {

		StringBuilder help = new StringBuilder();
		help.append("请输入过期时间：");
		while (true) {
			System.out.println(help.toString());
			String input = scanner.next();
			if (!StringUtils.isEmpty(input)) {
				System.out.println(input);
				return Integer.parseInt(input);
			}
		}

	}

}