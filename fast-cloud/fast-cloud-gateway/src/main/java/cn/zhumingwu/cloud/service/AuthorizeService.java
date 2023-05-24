package cn.zhumingwu.cloud.service;

public interface AuthorizeService {

    boolean checkAuthorize(String path, String[] userRoles);
}