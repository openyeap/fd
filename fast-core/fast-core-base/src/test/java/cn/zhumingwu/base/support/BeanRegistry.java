package cn.zhumingwu.base.support;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.AOPMethodTests;
import cn.zhumingwu.base.util.NamingUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeanRegistry implements BeanDefinitionRegistryPostProcessor {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerSingleton("DefaultFactoryBean", new DefaultFactoryBean(new AOPMethodTests.TestClass()));
        var list = beanFactory.getBeanNamesForType(FactoryBean.class);
        for (var item : list) {
            NamingUtils.formatLog(log,"postProcessBeanFactory:{}", item);
        }
        beanFactory.setBeanClassLoader(new MyClassLoader(beanFactory.getBeanClassLoader()));
    }
}
