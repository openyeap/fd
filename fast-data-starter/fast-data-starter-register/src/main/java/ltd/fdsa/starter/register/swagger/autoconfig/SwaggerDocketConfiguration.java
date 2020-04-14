package ltd.fdsa.starter.register.swagger.autoconfig;

import com.google.common.base.Predicate;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.starter.register.swagger.properties.ApiInfo;
import ltd.fdsa.starter.register.swagger.properties.Contact;
import ltd.fdsa.starter.register.swagger.properties.SwaggerProperties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.file.Path;
import java.util.UUID;

/**
 * @Classname SwaggerDocketConfiguration
 * @Description TODO
 * @Date 2019/12/16 11:48
 * @Author 高进
 */
@Slf4j
@EnableSwagger2
@SuppressWarnings("deprecation")
public class SwaggerDocketConfiguration implements BeanFactoryPostProcessor, EnvironmentAware {

    private Environment environment;
    private String applicationName;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private final String DEFAULT_APPLICATION_NAME = "Default";

    private SwaggerProperties getSwaggerProperties() {
        applicationName = environment.getProperty("spring.application.name");
        if (applicationName == null || "".equals(applicationName)) {
            applicationName = DEFAULT_APPLICATION_NAME;
        }
        return initSwaggerProperties();
    }

    private SwaggerProperties initSwaggerProperties() {
        SwaggerProperties swaggerProperties = Binder.get(environment).bind(SwaggerProperties.prefix, SwaggerProperties.class).get();
        if (swaggerProperties.getEnabled() == null) {
            swaggerProperties.setEnabled(false);
        }
        if (swaggerProperties.getBasePackage() == null || "".equals(swaggerProperties.getBasePackage())) {
            swaggerProperties.setBasePackage("ltd.fdsa");
        }
        swaggerProperties.setApiInfo(initApiInfo(swaggerProperties.getApiInfo()));
        swaggerProperties.setContact(initContact(swaggerProperties.getContact()));
        return swaggerProperties;
    }

    private final String DEFAULT_VERSION = "1.0";
    private final String DEFAULT_TITLE = "Api Documentation";
    private final String DEFAULT_DESCRIPTION = "Api Documentation";
    private final String DEFAULT_TERMS_URL = "";//"urn:tos";
    private final String DEFAULT_LICENSE = "";//"Apache 2.0";
    private final String DEFAULT_LICENSE_URL = "";//http://www.apache.org/licenses/LICENSE-2.0";

    private ApiInfo initApiInfo(ApiInfo apiInfo) {
        if (apiInfo == null) {
            apiInfo = new ApiInfo();
        }
        if (StringUtils.isEmpty(apiInfo.getVersion())) {
            apiInfo.setVersion(DEFAULT_VERSION);
        }
        if (StringUtils.isEmpty(apiInfo.getTitle())) {
            apiInfo.setTitle(DEFAULT_TITLE);
        }
        if (StringUtils.isEmpty(apiInfo.getDescription())) {
            apiInfo.setDescription(DEFAULT_DESCRIPTION);
        }
        if (StringUtils.isEmpty(apiInfo.getTermsOfServiceUrl())) {
            apiInfo.setTermsOfServiceUrl(DEFAULT_TERMS_URL);
        }
        if (StringUtils.isEmpty(apiInfo.getLicense())) {
            apiInfo.setLicense(DEFAULT_LICENSE);
        }
        if (StringUtils.isEmpty(apiInfo.getLicenseUrl())) {
            apiInfo.setLicenseUrl(DEFAULT_LICENSE_URL);
        }
        return apiInfo;
    }

    private final String DEFAULT_CONTACT_NAME = "";
    private final String DEFAULT_CONTACT_URL = "";
    private final String DEFAULT_CONTACT_EMAIL = "";

    private Contact initContact(Contact contact) {
        if (contact == null) {
            contact = new Contact();
        }
        if (StringUtils.isEmpty(contact.getName())) {
            contact.setName(DEFAULT_CONTACT_NAME);
        }
        if (StringUtils.isEmpty(contact.getUrl())) {
            contact.setName(DEFAULT_CONTACT_URL);
        }
        if (StringUtils.isEmpty(contact.getEmail())) {
            contact.setName(DEFAULT_CONTACT_EMAIL);
        }
        return contact;
    }

    private String[] spitBasePackage(String basePackage) {
        if (StringUtils.isEmpty(basePackage) || (basePackage = basePackage.trim()).isEmpty()) {
            return null;
        } else {
            return basePackage.split(",");
        }
    }

    private Docket getSwaggerDocket(final SwaggerProperties swaggerProperties) {
        final String[] basePackages = spitBasePackage(swaggerProperties.getBasePackage());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().version(swaggerProperties.getApiInfo().getVersion())
                        .title(swaggerProperties.getApiInfo().getTitle())
                        .description(swaggerProperties.getApiInfo().getDescription())
                        .license(swaggerProperties.getApiInfo().getLicense())
                        .licenseUrl(swaggerProperties.getApiInfo().getLicenseUrl())
                        .termsOfServiceUrl(swaggerProperties.getApiInfo().getTermsOfServiceUrl())
                        .contact(new springfox.documentation.service.Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                        .build())
                .groupName(applicationName).pathMapping("/")
                .select()
                .apis(new Predicate<RequestHandler>() {
                    @Override
                    public boolean apply(RequestHandler requestHandler) {
                        if (basePackages == null) {
                            return true;
                        }
                        String packageName = requestHandler.declaringClass().getName();
                        for (String basePackage : basePackages) {
                            if (packageName.startsWith(basePackage) || packageName.matches(basePackage + ".*")) {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .paths(PathSelectors.any())//可以配置正则表达式
                .build();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SwaggerProperties swaggerProperties = getSwaggerProperties();
        if (swaggerProperties != null) {
            Docket swaggerDocket = getSwaggerDocket(swaggerProperties);
            configurableListableBeanFactory.registerSingleton(applicationName, swaggerDocket);
        }
    }
}
