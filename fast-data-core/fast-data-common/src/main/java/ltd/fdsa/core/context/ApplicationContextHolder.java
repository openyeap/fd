package ltd.fdsa.core.context;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.event.RemoteEventPublisher;
import ltd.fdsa.core.event.RemotingEvent;
import ltd.fdsa.core.lock.LockManager;
import ltd.fdsa.core.lock.ReentrantLockManager;
import ltd.fdsa.core.util.NamingUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ApplicationContextHolder implements // BeanFactoryPostProcessor,
        ApplicationContextAware, InstantiationAwareBeanPostProcessor, ApplicationListener<RemotingEvent> {

    private static final ThreadLocal<Map<String, Object>> OBJECT_HOLDER = new NamedThreadLocal<>("objectHolder");

    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // NamingUtils.formatLog(log,"{} 初始化前 {}", beanName, bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // NamingUtils.formatLog(log,"{} 初始化后 {}", beanName, bean);
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        // NamingUtils.formatLog(log,"{} 实例化前 {}", beanName, beanClass);
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        // NamingUtils.formatLog(log,"{} 实例化后 {}", beanName, bean);
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
            throws BeansException {
        // NamingUtils.formatLog(log,"{} 属性设置", beanName);
        return pvs;
    }

    @Override
    public void onApplicationEvent(RemotingEvent event) {
        Class<ApplicationEvent> clazz = null;
        try {
            clazz = (Class<ApplicationEvent>) ClassUtils.forName(event.getName(), null);
        } catch (ClassNotFoundException e) {
            log.error("onApplicationEvent", e);
        }
        var applicationEvent = RemotingEvent.getApplicationEvent(event.getPayload(), clazz);
        if (applicationEvent != null) {
            ApplicationContextHolder.publishLocal(applicationEvent);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        NamingUtils.formatLog(log, "ApplicationContext has been set");
        APPLICATION_CONTEXT = applicationContext;
    }

    public static Map<String, CacheManager> getCacheManagers() {
        return APPLICATION_CONTEXT.getBeansOfType(CacheManager.class);
    }

    public static CacheManager getCacheManager(String... name) {
        if (name.length <= 0 || Strings.isNullOrEmpty(name[0])) {
            return new NoOpCacheManager();
        }
        var list = APPLICATION_CONTEXT.getBeansOfType(CacheManager.class);
        if (list.containsKey(name[0])) {
            return list.get(name[0]);
        }
        return new NoOpCacheManager();
    }

    public static Map<String, LockManager> getLockManagers() {
        return APPLICATION_CONTEXT.getBeansOfType(LockManager.class);
    }

    public static LockManager getLockManager(String name) {
        var list = APPLICATION_CONTEXT.getBeansOfType(LockManager.class);
        if (list.containsKey(name)) {
            return list.get(name);
        }
        return new ReentrantLockManager();
    }

    public static ThreadContext getThreadContext() {
        return new ThreadContext();
    }

    public static <T> T getBean(Class<T> requiredType) {
        return APPLICATION_CONTEXT.getBean(requiredType);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        return APPLICATION_CONTEXT.getBeansOfType(requiredType);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return APPLICATION_CONTEXT.getBeansWithAnnotation(annotationType);
    }

    public static void publishRemote(ApplicationEvent event) {
        if (event == null) {
            log.warn("Event must not be null");
            return;
        }
        var list = APPLICATION_CONTEXT.getBeansOfType(RemoteEventPublisher.class);
        if (list.size() <= 0) {
            log.warn("Event Service must not be null");
            publishLocal(event);
            return;
        }
        list.forEach((name, publisher) -> {
            publisher.send(new RemotingEvent(event));
        });
    }

    public static void publishLocal(ApplicationEvent event) {
        APPLICATION_CONTEXT.publishEvent(event);
    }

    static class ThreadContext {
        public <T> T get(String name) {
            Map<String, Object> map = OBJECT_HOLDER.get();
            if (map == null) {
                return null;
            }
            var result = map.get(name);
            if (result == null) {
                return null;
            }
            return (T) result;
        }

        public void set(String name, Object value) {
            Map<String, Object> map = OBJECT_HOLDER.get();
            if (map == null) {
                map = new HashMap<>();
                OBJECT_HOLDER.set(map);
            }
            map.put(name, value);
        }

        public void remove(String name) {
            Map<String, Object> map = OBJECT_HOLDER.get();
            if (map != null) {
                map.remove(name);
            }
        }

        public void clear() {
            Map<String, Object> map = OBJECT_HOLDER.get();
            if (map != null) {
                map.clear();
            }
        }
    }
}
