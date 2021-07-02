package ltd.fdsa.starter.remote;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.starter.remote.annotation.RpcClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class RpcApiBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, BeanFactoryAware {
    Environment environment;
    BeanFactory beanFactory;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        RpcApiClassScanner scanner = new RpcApiClassScanner(registry);
        NamingUtils.formatLog(log,"Before RpcApi Registry: " + registry.getBeanDefinitionCount());
        var list = AutoConfigurationPackages.get(this.beanFactory);
        var packages = this.environment.getProperty("project.rpc.packages", String[].class);
        if (packages != null) {
            for (var item : packages) {
                list.add(item);
            }
        }
        packages = StringUtils.toStringArray(list);

        scanner.scan(packages);
        for (var name : registry.getBeanDefinitionNames()) {
            var beanDefinition = registry.getBeanDefinition(name);
            if (beanDefinition instanceof GenericBeanDefinition && name.endsWith("ontroller")) {
                Class<?> clazz = null;
                try {
                    clazz = ClassUtils.forName(beanDefinition.getBeanClassName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz == null) {
                    continue;
                }
                for (var field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(RpcClient.class)) {

                        var originalClassName = field.getType().getCanonicalName();

                        if (registry.containsBeanDefinition(originalClassName)) {
                            continue;
                        }
                        var registryBeanDefinition = new GenericBeanDefinition();

                        registryBeanDefinition.setBeanClass(RpcMethodProxyFactoryBean.class);
                        registryBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
                        registryBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(field);
                        registryBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

                        registry.registerBeanDefinition(originalClassName, registryBeanDefinition);
                    }
                }
            }
        }

        NamingUtils.formatLog(log, "After RpcApi Registry: " + registry.getBeanDefinitionCount());
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}