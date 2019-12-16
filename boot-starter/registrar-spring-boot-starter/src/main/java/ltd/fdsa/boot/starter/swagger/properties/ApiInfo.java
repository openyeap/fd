package ltd.fdsa.boot.starter.swagger.properties;

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
    @Value("spring.swagger.apiInfo.version:1.0")
    private String version;
    @Value("spring.swagger.apiInfo.title:Api Documentation")
    private String title;
    @Value("spring.swagger.apiInfo.description:Api Documentation")
    private String description;
    @Value("spring.swagger.apiInfo.termsOfServiceUrl:urn:tos")
    private String termsOfServiceUrl;
    @Value("spring.swagger.apiInfo.license:Apache 2.0")
    private String license;
    @Value("spring.swagger.apiInfo.licenseUrl:http://www.apache.org/licenses/LICENSE-2.0")
    private String licenseUrl;
}
