package cn.zhumingwu.base.config;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.properties.ProjectProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableConfigurationProperties({ProjectProperties.class})
public class ProjectAutoConfiguration {
    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }
}
