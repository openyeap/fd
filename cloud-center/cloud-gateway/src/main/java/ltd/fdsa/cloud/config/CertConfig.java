package ltd.fdsa.cloud.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import ltd.fdsa.cloud.util.LicenseUtils;
import org.aspectj.util.FileUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname CertConfig
 * @Description TODO
 * @Date 2019/12/25 17:06
 * @Author 高进
 */
@Component
@Configuration
@ConfigurationProperties(CertConfig.prefix)
@Log4j2
public class CertConfig {
    public final static String prefix = "spring.daoshu.cert";

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    private String certPath = "/usr/local/cert/";//证书路径

    private int interval = 10;//校验间隔，单位是分钟

    private static Map<String, CertInfo> certs;

    /**
     * 校验该微服务，是否需要进行证书认证
     *
     * @param serverName
     * @return
     */
    public boolean needCheck(String serverName) {
        if (certs == null) {
            certs = new HashMap<>();
        }
        long currentTime = System.currentTimeMillis();
        CertInfo certInfo = null;
        if (certs.containsKey(serverName)) {
            certInfo = certs.get(serverName);
        } else {
            certInfo = new CertInfo();
        }
        if (certInfo.checkStatus == CheckStatus.NO_PASSING || currentTime - certInfo.getCheckTime() > interval * 60 * 1000) {
            certInfo.setCheckTime(currentTime);
            certInfo.setCheckStatus(CheckStatus.NO_PASSING);
            certs.put(serverName, certInfo);
            return true;
        }
        return false;
    }

    /**
     * 对该微服务进行证书认证
     *
     * @param serverName
     * @return
     * @throws Exception
     */
    public boolean checkCert(String serverName) throws Exception {
        if(certs == null) {
            return true;
        }
        CertInfo certInfo = certs.get(serverName);
        if(certInfo == null) {
            return true;
        }
        String publicPath = certPath + serverName + "/" + "public.pem";
        String snPath = certPath + serverName + "/" + "sn.pem";
        if (!checkPathExist(publicPath) || !checkPathExist(snPath)) {
            throw new Exception("服务【" + serverName + "】证书信息未配置，请联系管理员");
        }
        String publicKey = readFile(publicPath);
        String sn = readFile(snPath);
        if ("".equals(publicKey) || "".equals(sn)) {
            return false;
        }
        boolean checkFlag = LicenseUtils.verifySerialNumber(publicKey, sn);
        if(checkFlag) {
            certInfo.setCheckStatus(CheckStatus.PASSING);
            certInfo.setCheckTime(System.currentTimeMillis());
        } else {
            certInfo.setCheckStatus(CheckStatus.NO_PASSING);
        }
        return checkFlag;
    }

    private boolean checkPathExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    private String readFile(String fileName) {
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

    @Data
    public static class CertInfo {
        private CheckStatus checkStatus = CheckStatus.NO_PASSING;//校验状态
        private long checkTime = 0l;//校验时间
    }

    public enum CheckStatus {
        NO_PASSING,
        PASSING
    }
}
