package cn.zhumingwu.koffer;

import cn.zhumingwu.koffer.key.XKey;

import java.util.Scanner;

/**
 * Spring-Boot 启动器
 */
public class XLauncher implements XConstants {
    public final String[] args;
    public final XDecryptor xDecryptor;
    public final XEncryptor xEncryptor;
    public final XKey xKey;

    public XLauncher(String... args) throws Exception {
        this.args = args;
        Scanner scanner = new Scanner(System.in);
        String algorithm = scanner.nextLine();
        int keysize = Integer.parseInt(scanner.nextLine());
        int ivsize = Integer.parseInt(scanner.nextLine());
        String password = scanner.nextLine();
        this.xDecryptor = new XJdkDecryptor();
        this.xEncryptor = new XJdkEncryptor();
        this.xKey = XKit.key(algorithm, keysize, ivsize, password);
    }
}
