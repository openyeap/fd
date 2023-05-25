package cn.zhumingwu.starter.remote;

import cn.zhumingwu.starter.remote.annotation.RpcClient;
import cn.zhumingwu.starter.remote.hessian.HessianTraceConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.event.ServiceDiscoveredEvent;
import cn.zhumingwu.base.service.InstanceInfo;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.Retryable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RpcMethodProxyFactoryBean<T> implements FactoryBean<T>, EnvironmentAware, ApplicationListener<ServiceDiscoveredEvent>, InitializingBean {
    private static Retryable DEFAULT_RETRY;

    static {
        // 默认注解

        @Retryable
        final class c {
        }
        DEFAULT_RETRY = c.class.getAnnotation(Retryable.class);
    }

    private static final HessianProxyFactory factory = new HessianProxyFactory();
    /**
     * 线程安全的
     */
    private final static AtomicInteger next = new AtomicInteger(0);

    private final Class<?> proxyClass;
    private final Class<?> clazz;
    private final Field field;
    private Environment environment;

    public RpcMethodProxyFactoryBean(Class<?> clazz, Field field) {
        this.clazz = clazz;
        this.field = field;
        this.proxyClass = field.getType();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public T getObject() {
        var proxy = Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class<?>[]{proxyClass},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        //获取拦截的方法中的@Retryable
                        Retryable retryable = method.getAnnotation(Retryable.class);
                        if (retryable == null) {
                            retryable = DEFAULT_RETRY;
                        }
                        int attempts = 0;
                        while (true) {
                            try {
                                return innerInvoke(method, args);
                            } catch (Exception e) {
                                if (attempts >= retryable.maxAttempts()) {
                                    throw new Exception("invoke service maxAttempts:" + attempts, e);
                                }
                                Thread.sleep(0);
                            }
                            attempts++;
                            log.info("attempt invoke service:{}.{} {} times", proxyClass, method.getName(), attempts);
                        }
                    }

                    private Object innerInvoke(Method method, Object[] args) throws Exception {
                        var service = createRpcService();
                        if (service == null) {
                            throw new Exception("create remote service api failed");
                        }
                        return service.invoke(proxyClass, method.getName(), args);
                    }
                }
        );
        if (proxy == null) {
            return null;
        }
        return (T) proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return this.proxyClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    Map<String, List<InstanceInfo>> serviceList = Maps.newHashMap();

    private RpcService createRpcService() {
        try {
            var api = this.field.getAnnotation(RpcClient.class);
            var serviceName = this.environment.resolvePlaceholders(api.value());
            if (!serviceList.containsKey(serviceName)) {
                return null;
            }
            var list = serviceList.get(serviceName).toArray(new InstanceInfo[0]);
            if (list.length == 0) {
                return null;
            }
            var service = list[next.getAndIncrement() % list.length];

            var url = String.format("http://%s:%s/hessian/rpc/service", service.getIp(), service.getPort());
            return (RpcService) factory.create(RpcService.class, url);
        } catch (MalformedURLException e) {
            log.error("createRpcService", e);
            return null;
        }
    }

    @Override
    public void onApplicationEvent(ServiceDiscoveredEvent event) {
        this.serviceList = event.getServices();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        var connectionFactory = new HessianTraceConnectionFactory();
        connectionFactory.setHessianProxyFactory(factory);
        factory.setConnectionFactory(connectionFactory);
    }
}
