package ltd.fdsa.boot.starter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config  {
	@Value("${swagger.package:ltd.fdsa}")
	String packageName;
	@Value("${swagger.title:default title}")
	String title;
	@Value("${swagger.description:default description}")
	String description;
	@Value("${swagger.version:1.0}")
	String version;
	@Value("${swagger.url.term:http://fdsa.ltd}")
	String termUrl;
	@Value("${swagger.url.contact:http://fdsa.ltd}")
	String contactUrl;
	@Value("${swagger.name:default name}")
	String name;
	@Value("${swagger.email:default@fdsa.ltd}")
	String email;

	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(this.packageName))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(this.title)
                .description(this.description)
				.version(this.version)// 版本
                .termsOfServiceUrl(this.termUrl)
                .contact(new Contact(this.name,this.contactUrl,this.email))
                .build();
    }

}
