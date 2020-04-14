package ltd.fdsa.starter.register.swagger.autoconfig;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Classname ConditionNotApi
 * @Description TODO
 * @Date 2019/12/16 10:48
 * @Author 高进
 */
public class ConditionNotApi implements Condition {
    ConditionApi conditionApi = new ConditionApi();

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return !conditionApi.matches(conditionContext, annotatedTypeMetadata);
    }
}
