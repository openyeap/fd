//package ltd.fdsa.cloud.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//@Configuration
//public class StaticRoute {
//    @Bean
//    RouterFunction<ServerResponse> staticResourceRouter(){ 
//        return RouterFunctions.resources("classpath:/META-INF/resources/",
//                new ClassPathResource("classpath:/META-INF/resources/webjars/"));
//        return RouterFunctions.resources("/swagger-ui.html**",
//                new ClassPathResource("classpath:/META-INF/resources/"));
//    }
//}