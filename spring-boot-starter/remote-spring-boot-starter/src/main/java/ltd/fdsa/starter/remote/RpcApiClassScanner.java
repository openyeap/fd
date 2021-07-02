package ltd.fdsa.starter.remote;

import lombok.SneakyThrows;
import lombok.var;
import ltd.fdsa.starter.remote.annotation.RpcApis;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Set;

public class RpcApiClassScanner extends ClassPathBeanDefinitionScanner {
    public RpcApiClassScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        AnnotationTypeFilter typeFilter = new AnnotationTypeFilter(RpcApis.class);
        addIncludeFilter(typeFilter);
        addIncludeFilter(new AnnotationTypeFilter(Component.class));
    }

    @SneakyThrows
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitions) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            var originalClassName = beanDefinition.getBeanClassName();

            beanDefinition.setBeanClass(RpcClientProxyFactoryBean.class);
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(originalClassName);
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
        return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            BeanDefinitionRegistry registry = getRegistry();
            try {
                var beanClass = ClassUtils.forName(beanDefinition.getBeanClassName(), null);
                for (String name : registry.getBeanDefinitionNames()) {
                    BeanDefinition definedBeanDefinition = registry.getBeanDefinition(name);
                    if (definedBeanDefinition.getBeanClassName() == null) {
                        break;
                    }
                    var definedBeanClass = ClassUtils.forName(definedBeanDefinition.getBeanClassName(), null);
                    if (beanClass.isAssignableFrom(definedBeanClass)) {
                        return false;
                    }
                }

            } catch (ClassNotFoundException e) {
                logger.error("ClassNotFoundException", e);
            }
            return true;
        }
        return false;

    }
}
