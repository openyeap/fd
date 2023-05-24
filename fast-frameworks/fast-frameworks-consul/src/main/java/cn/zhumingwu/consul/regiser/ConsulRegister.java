package cn.zhumingwu.consul.regiser;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.core.context.ApplicationContextHolder;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ConsulRegister {
    static Map<String, String> urlRoles;

    private Map<String, String> createAuthConfig() {
        if (urlRoles != null) {
            return urlRoles;
        }
        urlRoles = new HashMap<>();

        Map<String, Object> restControllers = ApplicationContextHolder.getBeansWithAnnotation(RestController.class);
        if (restControllers == null || restControllers.isEmpty()) {
            return null;
        }

        for (var entry : restControllers.entrySet()) {
            String[] classUrls;
            RequestMapping classPath = AnnotationUtils.findAnnotation(entry.getClass(), RequestMapping.class);
            if (classPath != null) {
                classUrls = classPath.value();
            } else {
                classUrls = new String[]{"/"};
            }

            for (Method method : entry.getValue().getClass().getDeclaredMethods()) {
                Set<String> methodUrls = new HashSet<>();
                RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
                if (requestMapping != null) {
                    for (var item : requestMapping.value()) {
                        methodUrls.add(item);
                    }
                }
                PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
                if (postMapping != null) {
                    for (var item : postMapping.value()) {
                        methodUrls.add(item);
                    }
                }
                GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
                if (getMapping != null) {
                    for (var item : getMapping.value()) {
                        methodUrls.add(item);
                    }
                }
                if (methodUrls.size() <= 0) {
                    continue;
                }
                // 1、获取权限注解
                String role;
                var authRole = method.getAnnotation(Role.class);
                if (authRole != null) {
                    role = authRole.value() + "";
                } else {
                    role = "";
                }
                for (String urlClass : classUrls) {
                    for (String urlMethod : methodUrls) {
                        var key = StringUtils.cleanPath(urlClass + "/" + urlMethod).replace("//", "/");
                        urlRoles.put(key, role);
                    }
                }
            }
        }
        return urlRoles;
    }
}