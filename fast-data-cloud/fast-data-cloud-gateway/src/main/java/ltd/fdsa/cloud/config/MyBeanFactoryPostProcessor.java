package ltd.fdsa.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {


    /**
     * 本方法在Bean对象实例化之前执行，
     * 通过beanFactory可以获取bean的定义信息，
     * 并可以修改bean的定义信息。这点是和BeanPostProcessor最大区别
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerSingleton("myBeanName", ">> BeanFactoryPostProcessor 开始执行了");
        System.out.println(">> BeanFactoryPostProcessor 开始执行了");
        String[] names = beanFactory.getBeanDefinitionNames();

        System.out.println(names.length);
        for (String name : names) {
            System.out.println(">> BeanDefinition Name:" + name);
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

            if (propertyValues.contains("name")) {
                propertyValues.addPropertyValue("name", "bobo");
                System.out.println("修改了属性信息");
            }
        }
        System.out.println(">> BeanFactoryPostProcessor 执行结束");
    }
}
