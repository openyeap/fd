package cn.zhumingwu.base.support;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.util.NamingUtils;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Primary
public class PersonFactoryBean implements FactoryBean<Person> {

    final ProxyFactory factory;

    public PersonFactoryBean() {
        this.factory = new ProxyFactory(new Person());
        PersonPointCut pointCut = new PersonPointCut();
        Advice advice = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                NamingUtils.formatLog(log,"Primary say: {}", methodInvocation.getArguments()[0]);
                return null;
            }
        };
        Advisor advisor = new DefaultPointcutAdvisor(pointCut, advice);
        this.factory.addAdvisor(advisor);
    }

    @Override
    public Person getObject() throws Exception {
        return (Person) this.factory.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return Person.class;
    }


}
