package ltd.fdsa.web.properties;

import io.swagger.models.Contact;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(SwaggerProperties.PREFIX)
public class SwaggerProperties {
    public static final String PREFIX = "project.swagger";

    private boolean enabled;
    private boolean grouped;
    private String[] basePackages;
    private ApiInfo apiInfo = new ApiInfo();

    @Data
    public static class ApiInfo {
        private String version;
        private String title;
        private String description;
        private String termsOfServiceUrl;
        private String license;
        private String licenseUrl;
        private Contact contact = new Contact();
    }

}
