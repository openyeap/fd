package ltd.fdsa.core.context;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultApplicationContextAware implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // NamingUtils.formatLog(log, "{}\n{}", "在容器刷新之前调用此类的initialize方法。用户可以在整个spring容器还没被初始化之前做一些事情。", "在最开始激活一些配置，或者利用这时候class还没被类加载器加载的时机，进行动态字节码注入等操作");
    }
}

