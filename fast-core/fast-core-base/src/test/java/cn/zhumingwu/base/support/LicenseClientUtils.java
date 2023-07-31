package cn.zhumingwu.base.support;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.base.util.LicenseUtils;
import cn.zhumingwu.base.util.RSAUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

/**
 * java获取机器码，注册码的实现
 */
@Slf4j
public class LicenseClientUtils {


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


    public static boolean generatePKI(String root) {
        var kp = RSAUtils.genKeyPair();
        String privateKey = kp.getPrivateKey();
        String publicKey = kp.getPublicKey();
        String filePath = root + "/public.pem";
        File writeFile = new File(filePath);
        if (!writeFile.exists()) {
            writeFile.createNewFile();
        }
        try (FileWriter fileWriter = new FileWriter(writeFile);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(publicKey);
            bufferedWriter.flush();
        }

        filePath = root + "/private.pem";
        writeFile = new File(filePath);
        if (!writeFile.exists()) {
            writeFile.createNewFile();
        }
        try (FileWriter fileWriter = new FileWriter(writeFile);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(privateKey);
            bufferedWriter.flush();
        }

        return true;
    }
}
