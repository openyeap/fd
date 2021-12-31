package ltd.fdsa.core.config;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.context.ApplicationContextHolder;
import ltd.fdsa.core.properties.ProjectProperties;
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
