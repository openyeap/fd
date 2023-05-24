package cn.zhumingwu.cloud.service.impl;

import cn.zhumingwu.cloud.service.AuthorizeService;
import com.netflix.appinfo.InstanceInfo;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class AuthorizeServiceImpl implements ApplicationListener<InstanceRegisteredEvent<InstanceInfo>>, AuthorizeService {

    private static ConcurrentHashMap<String, String> authList = new ConcurrentHashMap<String, String>();
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public boolean checkAuthorize(String path, String[] userRoles) {
        AntPathMatcher matcher = new AntPathMatcher();

        String[] accessRoles = null;
        for (Map.Entry<String, String> entry : authList.entrySet()) {
            if (matcher.match(entry.getKey(), path)) {
                accessRoles = entry.getValue().split(",");
                break;
            }
        }
        if (accessRoles != null) {
            List<String> accessRoleList = Arrays.asList(accessRoles);
            if (accessRoleList.contains("*")) {
                return true;
            }
            if (userRoles == null || userRoles.length == 0) {
                return false;
            }
            for (String userRole : userRoles) {
                if (accessRoleList.contains(userRole)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void onApplicationEvent(InstanceRegisteredEvent<InstanceInfo> event) {
        var config = event.getConfig();
        var map = (Map<String, String>) config.getMetadata();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            authList.put(entry.getKey(), entry.getValue());
        }

    }
}
