package cn.zhumingwu.starter.register.model;

import cn.zhumingwu.base.service.ServiceMetaDataProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.context.ApplicationContextHolder;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class Register implements ServiceMetaDataProvider {
    static Map<String, String> urlRoles = new HashMap<>();

    @Override
    public Map<String, String> metadata() {
        if (!urlRoles.isEmpty() ) {
            return urlRoles;
        }
        Map<String, Object> restControllers = ApplicationContextHolder.getBeansWithAnnotation(RestController.class);
        if (restControllers.isEmpty()) {
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
                    methodUrls.addAll(Arrays.asList(requestMapping.value()));
                }
                PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
                if (postMapping != null) {
                    methodUrls.addAll(Arrays.asList(postMapping.value()));
                }
                GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
                if (getMapping != null) {
                    methodUrls.addAll(Arrays.asList(getMapping.value()));
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

