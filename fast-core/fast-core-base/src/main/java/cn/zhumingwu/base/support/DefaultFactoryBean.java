package cn.zhumingwu.base.support;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;


@Slf4j
public class DefaultFactoryBean<T> implements FactoryBean<T> {
    final ProxyFactory factory;
    private final Class<?> targetClass;

    public DefaultFactoryBean(T target) {
        this.targetClass = target.getClass();
        this.factory = new ProxyFactory(target);
        Advice advice = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                log.info("Primary say: {}", methodInvocation.getMethod());
                return methodInvocation.proceed();
            }
        };
        this.factory.addAdvice(advice);
    }

    @Override
    public T getObject() throws Exception {
        return (T) this.factory.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return this.targetClass;
    }

}



