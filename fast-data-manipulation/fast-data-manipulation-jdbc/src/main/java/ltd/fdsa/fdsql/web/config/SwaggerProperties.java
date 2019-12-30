package ltd.fdsa.fdsql.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Classname SwaggerProperties
 * @Description TODO
 * @Date 2019/12/27 16:20
 * @Author 高进
 */
@ConfigurationProperties(prefix = SwaggerProperties.prefix)
@Data
@Component
public class SwaggerProperties {
    public final static String prefix = "spring.daoshu.swagger.info";

    private String version = "1.0";
    private String title = "Default";
    private String description = "Api Document";
    private String[] license = {};
    private Contact[] contact = {};

    @Data
    private static class Contact {
        private String name;
        private String url;
        private String email;
    }
}
