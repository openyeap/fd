package cn.zhumingwu.base;

import cn.zhumingwu.base.support.Person;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.config.ProjectAutoConfiguration;
import cn.zhumingwu.base.util.NamingUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.Advisor;
import org.springframework.aop.DynamicIntroductionAdvice;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ProjectAutoConfiguration.class)
public class AOPClassTests {

    @Test
    public void TestPersonExtension() {
        ProxyFactory factory = new ProxyFactory(new Person());
        // 强制私用CGLIB 以保证我们的Person方法也能正常调用
        factory.setProxyTargetClass(true);
        // 此处采用IntroductionInterceptor 这个引介增强的拦截器
        DynamicIntroductionAdvice advice = new RunDelegatingIntroductionInterceptor();
//        advice = new DelegatePerTargetObjectIntroductionInterceptor(PersonRun.class, Run.class);

        // 切点+通知（注意：此处放的是复合切面）
        Advisor advisor = new DefaultIntroductionAdvisor(advice, Run.class);
        factory.addAdvisor(advisor);

        // Person本身功能
        Person p = (Person) factory.getProxy();
        p.say("something");

        // 扩展功能
        Run extension = (Run) factory.getProxy();
        extension.run();
    }

    interface Run {
        void run();
    }

    static class RunDelegatingIntroductionInterceptor extends DelegatingIntroductionInterceptor implements Run {

        @Override
        public void run() {
            NamingUtils.formatLog(log, "I can run");
        }
    }


    // 自己定义一个IntroductionInterceptor来实现IntroductionInterceptor接口
    static class RunIntroductionInterceptor implements IntroductionInterceptor, Run {
        /**
         * 判断调用的方法是否为指定类中的方法
         * 如果Method代表了一个方法 那么调用它的invoke就相当于执行了它代表的这个方法
         */
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (implementsInterface(invocation.getMethod().getDeclaringClass())) {
                return invocation.getMethod().invoke(this, invocation.getArguments());
            }
            return invocation.proceed();
        }

        /**
         * 判断clazz是否为给定接口IOtherBean的实现
         */
        @Override
        public boolean implementsInterface(Class<?> clazz) {
            return clazz.isAssignableFrom(Run.class);
        }

        @Override
        public void run() {
            NamingUtils.formatLog(log, "I can run");
        }
    }

}
