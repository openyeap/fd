package ltd.fdsa.cloud.service;

public interface ConsulService {

    boolean checkAuthorize(String path, String[] userRoles);
}