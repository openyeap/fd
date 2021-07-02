package ltd.fdsa.switcher.core.job.executor;

import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.switcher.core.job.Job;
import ltd.fdsa.switcher.core.job.handler.impl.MethodJobHandler;
import ltd.fdsa.web.view.Result;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Properties;

public class JobSpringExecutor extends JobExecutor implements ApplicationContextAware, InitializingBean, DisposableBean {

    // ---------------------- applicationContext ----------------------
    private static ApplicationContext applicationContext;

    public JobSpringExecutor(Properties properties) {
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

        // refresh GlueFactory

        // super start
        super.start();
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
                Job Job = AnnotationUtils.findAnnotation(method, Job.class);
                if (Job != null) {

                    // name
                    String name = Job.value();
                    if (name.trim().length() == 0) {
                        throw new RuntimeException(
                                NamingUtils.format("job handler name invalid, for[{}.{}].",
                                        bean.getClass(), method.getName()));
                    }
                    if (loadJobHandler(name) != null) {
                        throw new RuntimeException(NamingUtils.format(" job handler [{}] naming conflicts.", name));
                    }

                    // execute method
                    if (method.getParameterTypes().length != 1 || !method.getReturnType().isAssignableFrom(Result.class)) {
                        throw new RuntimeException(
                                "job handler method invalid, for["
                                        + bean.getClass()
                                        + "#"
                                        + method.getName()
                                        + "] , "
                                        + "The correct method format like \" public Result<T> execute(Object param) \" .");
                    }

                    method.setAccessible(true);

                    // init and destroy Method
                    Method initMethod = null;
                    Method destroyMethod = null;

                    if (Job.init().trim().length() > 0) {
                        try {
                            initMethod = bean.getClass().getDeclaredMethod(Job.init());
                            initMethod.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(
                                    "job handler initMethod invalid, for["
                                            + bean.getClass()
                                            + "#"
                                            + method.getName()
                                            + "] .");
                        }
                    }
                    if (Job.destroy().trim().length() > 0) {
                        try {
                            destroyMethod = bean.getClass().getDeclaredMethod(Job.destroy());
                            destroyMethod.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(
                                    "job handler destroyMethod invalid, for["
                                            + bean.getClass()
                                            + "#"
                                            + method.getName()
                                            + "] .");
                        }
                    }
                    // registry job handler
                    registerJobHandler(name, new MethodJobHandler(bean, method, initMethod, destroyMethod));
                }
            }
        }
    }
}
