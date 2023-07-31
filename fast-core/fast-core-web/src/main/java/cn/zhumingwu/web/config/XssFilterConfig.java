package cn.zhumingwu.web.config;

import cn.zhumingwu.web.properties.ProjectProperties;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * xss过滤拦截器
 *
 */
@Configuration
public class XssFilterConfig {
    private static final int FILTER_ORDER = 1;

    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean(ProjectProperties properties) {
        ProjectProperties.Xxs propertiesXxs = properties.getXxs();
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
        registration.setOrder(FILTER_ORDER);
        registration.setEnabled(propertiesXxs.isEnabled());
        registration.addUrlPatterns(propertiesXxs.getUrlPatterns().split(","));
        Map<String, String> initParameters = new HashMap<>(16);
        initParameters.put("excludes", propertiesXxs.getExcludes());
        registration.setInitParameters(initParameters);
        return registration;
    }
}
