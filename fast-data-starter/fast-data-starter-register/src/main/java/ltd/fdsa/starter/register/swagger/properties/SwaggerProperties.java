package ltd.fdsa.starter.register.swagger.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Classname SwaggerProperties
 * @Description TODO
 * @Date 2019/12/16 11:34
 * @Author 高进
 */
@Data
@ConfigurationProperties(SwaggerProperties.prefix)
public class SwaggerProperties {
    public static final String prefix = "spring.swagger";

    private Boolean enabled;

    private ApiInfo apiInfo = new ApiInfo();

    private Contact contact = new Contact();

    private String basePackage;
}
