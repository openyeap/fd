package cn.zhumingwu.dataswitch.core.job.executor;


import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.handler.IJobHandler;
import cn.zhumingwu.dataswitch.core.job.handler.annotation.JobHandler;
import com.google.common.base.Strings;
import lombok.var;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Properties;

public class SpringJobExecutor extends JobExecutor implements ApplicationContextAware, InitializingBean, DisposableBean {

    // ---------------------- applicationContext ----------------------
    private static ApplicationContext applicationContext;

    public SpringJobExecutor(Properties properties) {
        super(properties);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // start
    @Override
    public void afterPropertiesSet() throws Exception {
        // init JobHandler Repository (for method)
        initJobHandlerMethodRepository(applicationContext);
        // init JobHandler Repository (for class)
        var map = applicationContext.getBeansOfType(IJobHandler.class);
        for (var entry : map.entrySet()) {
            var name = entry.getKey();
            if (getJobHandler(name) != null) {
                throw new RuntimeException(MessageFormat.format(" job handler [{}] naming conflicts.", name));
            }
            registerJobHandler(entry.getKey(), entry.getValue());
        }
    }

    // destroy
    @Override
    public void destroy() {
        super.destroy();
    }

    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }

        // init job handler from method
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                JobHandler job = AnnotationUtils.findAnnotation(method, JobHandler.class);
                if (job == null) {
                    continue;
                }
                // name
                String name = job.value();
                if (Strings.isNullOrEmpty(name)) {
                    throw new RuntimeException(MessageFormat.format("job handler name invalid, for[{}.{}].", bean.getClass(), method.getName()));
                }
                if (getJobHandler(name) != null) {
                    throw new RuntimeException(MessageFormat.format(" job handler [{}] naming conflicts.", name));
                }
                // execute method
                if (method.getParameterTypes().length != 1 || !method.getReturnType().isAssignableFrom(Result.class)) {
                    throw new RuntimeException(
                            MessageFormat.format("job handler method invalid, for[{}.{}]. The correct method format like \" public Result<T> execute(Object param) \" .", bean.getClass(), method.getName())
                    );
                }

                method.setAccessible(true);

                // init and destroy Method
                Method initMethod = null;
                Method destroyMethod = null;

                if (job.init().trim().length() > 0) {
                    try {
                        initMethod = bean.getClass().getDeclaredMethod(job.init());
                        initMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("job handler initMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                }
                if (job.destroy().trim().length() > 0) {
                    try {
                        destroyMethod = bean.getClass().getDeclaredMethod(job.destroy());
                        destroyMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(
                                "job handler destroyMethod invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                    }
                }
                // registry job handler
                registerJobHandler(name, new MethodJobHandler(bean, method, initMethod, destroyMethod));
            }
        }
    }

    private static class MethodJobHandler extends IJobHandler {

        private final Object target;
        private final Method executeMethod;
        private final Method initMethod;
        private final Method destroyMethod;

        public MethodJobHandler(Object target, Method executeMethod, Method initMethod, Method destroyMethod) {
            this.target = target;
            this.executeMethod = executeMethod;
            this.initMethod = initMethod;
            this.destroyMethod = destroyMethod;
        }


        @Override
        public void init() throws Exception {
            if (initMethod != null) {
                initMethod.invoke(target);
            }
        }

        @Override
        public void execute() throws Exception {
            if (executeMethod != null) {
                executeMethod.invoke(target);
            }
        }

        @Override
        public void destroy() throws Exception {
            if (destroyMethod != null) {
                destroyMethod.invoke(target);
            }
        }

        @Override
        public String toString() {
            return super.toString() + "[" + target.getClass() + "#" + executeMethod.getName() + "]";
        }
    }

}
