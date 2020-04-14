package ltd.fdsa.starter.register.swagger.autoconfig;

 
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @Classname SwaggerAutoConfiguration
 * @Description TODO
 * @Date 2019/12/16 11:47
 * @Author 高进
 */
@Configuration
@Slf4j
public class SwaggerAutoConfiguration {
    @Configuration
    @Conditional(ConditionApi.class)
    public static class swaggerDocket extends SwaggerDocketConfiguration {
        {
            log.warn( "启用了swagger文档");
        }
    }

    @RestController
    @Conditional(ConditionNotApi.class)
    public static class PreventSwaggerUi extends PreventSwaggerResourcesController {
        {
            log.warn("禁用了swagger文档html页面‘/swagger-ui.html’和其他资源的访问");
        }
    }
}
