package ltd.fdsa.starter.register.swagger.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Classname ApiInfo
 * @Description TODO
 * @Date 2019/12/16 14:27
 * @Author 高进
 */
@Data
public class ApiInfo {
    private String version;
    private String title;
    private String description;
    private String termsOfServiceUrl;
    private String license;
    private String licenseUrl;
}
