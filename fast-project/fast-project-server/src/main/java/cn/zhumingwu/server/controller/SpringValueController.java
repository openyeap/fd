package cn.zhumingwu.server.controller;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.refresh.RefreshOperation;
import cn.zhumingwu.base.refresh.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RefreshScope
@Slf4j
public class SpringValueController {

    @Autowired
    private RefreshOperation propertiesOperation;

    @Autowired
    private Environment environment;

    @Value("${spring.application.name:default}")
    private String applicationName;

    @RequestMapping("/test")
    public Object test() {

        return new Object() {
            public String getApplication() {
                return applicationName;
            }
            public String getName() {
                return environment.getProperty("spring.application.name");
            }
        };
    }

    @RequestMapping("/update")
    public String update(String key, String value) {
        try {
            propertiesOperation.update(key, value);
            return "已添加该值";
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/refresh")
    public String refresh() {
        try {
            propertiesOperation.refresh();
            return "Value注解属性值已刷新";
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
