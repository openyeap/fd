package cn.zhumingwu.starter.register.model;

import lombok.Data;

import java.util.Map;

@Data
public class ServiceInstanceInfo {
    private String app;
    private String ipAddr;
    private String vipAddress;
    private String secureVipAddress;
    private String status;
    private int port;
    private String securePort;
    private String homePageUrl;
    private String statusPageUrl;
    private String healthCheckUrl;
    private DataCenterInfo dataCenterInfo;

    private Map<String, String> metadata;
}


