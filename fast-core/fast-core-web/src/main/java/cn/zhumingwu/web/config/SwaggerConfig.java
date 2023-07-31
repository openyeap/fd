package cn.zhumingwu.web.config;

import cn.zhumingwu.base.util.NamingUtils;

import cn.zhumingwu.web.properties.SwaggerProperties;
import java.util.function.Predicate;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "project.swagger.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class SwaggerConfig {
    @Autowired
    private SwaggerProperties properties;
    @Value("${spring.application.name:default}")
    private String applicationName;

    @Bean
    public Docket swaggerDocket() {
        NamingUtils.formatLog(log, "swaggerDocket");
        var groupName = this.properties.isGrouped() ? this.applicationName : Docket.DEFAULT_GROUP_NAME;
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(groupName)
                .pathMapping("/")
                .select()
                .apis(this.scanBasePackage())
                .paths(PathSelectors.any()) // 可以配置正则表达式
                .build();
    }

    private ApiInfo apiInfo() {
        var api = this.properties.getApiInfo();

        return new ApiInfoBuilder()
                .version(api.getVersion())
                .title(api.getTitle())
                .description(api.getDescription())
                .license(api.getLicense())
                .licenseUrl(api.getLicenseUrl())
                .termsOfServiceUrl(api.getTermsOfServiceUrl())
                .contact(
                        new springfox.documentation.service.Contact(
                                api.getContact().getName(),
                                api.getContact().getUrl(),
                                api.getContact().getEmail()))
                .build();
    }

    private Predicate<RequestHandler> scanBasePackage() {
        Predicate<RequestHandler> predicate = null;
        for (var basePackage : this.properties.getBasePackages()) {
            if (!Strings.isNullOrEmpty(basePackage)) {
                Predicate<RequestHandler> tempPredicate = RequestHandlerSelectors.basePackage(basePackage);
                predicate = predicate == null ? tempPredicate : predicate.or(tempPredicate);
            }
        }
        if (predicate == null) {
            throw new NullPointerException("basePackage配置不正确");
        }
        return predicate;
    }
}
