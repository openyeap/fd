package cn.zhumingwu.base.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

/**
 * java获取机器码，注册码的实现
 */
@Slf4j
public class LicenseUtils {
    private static final int SPLIT_LENGTH = 4;

    public static String getMachineCode(String salt, String ip) {
        StringBuffer result = new StringBuffer();
        String mac = getMacByIP(ip);
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
        String code = CheckSum(result.toString());
        return getSplitString(code, "-", SPLIT_LENGTH);
    }

    public static boolean verifySerialNumber(String publicKey, String serialNumber, String salt, String ip) {
        String machineCode = getMachineCode(salt, ip).replace("-", "");
        String body = serialNumber.substring(0, 16);
        String sign = serialNumber.substring(16);
        if (!sign.equals(CheckSum(body + machineCode, sign, publicKey))) {
            return false;
        }
        Long validTime = Long.valueOf(body, 16);
        Calendar rightNow = Calendar.getInstance();
        if (rightNow.getTimeInMillis() * 1000 > validTime) {
            return false;
        }
        return true;
    }

    public static String getExpiredDate(String publicKey, String serialNumber, String salt, String ip) {
        String machineCode = getMachineCode(salt, ip).replace("-", "");
        String body = serialNumber.substring(0, 16);
        String sign = serialNumber.substring(16);
        if (!sign.equals(CheckSum(body + machineCode, sign, publicKey))) {
            return null;
        }
        Long validTime = Long.valueOf(body, 16);
        Date date = new Date(validTime / 1000);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String CheckSum(String input, String privateKey, String publicKey) {
        if (publicKey != null && !"".equals(publicKey)) {
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
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length == 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
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

                return bytesToHexString(mac);
            }
        } catch (Exception ex) {
            log.error("LicenseUtils.getMac", ex);
        }
        return null;
    }

    private static String getMacByIP(String ipString) {
        byte[] mac = null;
        try {
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
            //获取与传入IP一致的mac
            while (el.hasMoreElements()) {
                NetworkInterface networkInterface = el.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ipString.equals(ip.getHostAddress())) {
                        mac = networkInterface.getHardwareAddress();
                        if (mac == null)
                            continue;
                        else
                            break;
                    }
                }
            }
            //传入IP未获取到mac，则默认取第一个
            if (mac == null) {
                el = NetworkInterface.getNetworkInterfaces();
                while (el.hasMoreElements()) {
                    NetworkInterface networkInterface = el.nextElement();
                    mac = networkInterface.getHardwareAddress();
                    if (mac == null)
                        continue;
                    else
                        break;
                }
            }
        } catch (Exception ex) {
            log.error("LicenseUtils.getMacByIP", ex);
        }
        return bytesToHexString(mac);
    }
}
