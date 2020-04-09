package ltd.fdsa.boot.starter.swagger.autoconfig;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Classname ConditionApi
 * @Description TODO
 * @Date 2019/12/16 10:42
 * @Author 高进
 */
public class ConditionApi implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        if (environment != null) {
            String enabled = environment.getProperty("spring.swagger.enabled");
            if ("true".equals(enabled)) {
                return true;
            }
        }
        return false;
    }
}
