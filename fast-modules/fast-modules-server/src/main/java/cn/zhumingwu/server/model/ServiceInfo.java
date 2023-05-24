package cn.zhumingwu.server.model;

import lombok.Data;

import java.util.List;

@Data
public class ServiceInfo {
    private String id;
    private String name;
    private List<String> tags;
    private String address;
    private int port;
    private String url;
    private String status; // 0-不可访问，1-可以访问
}
