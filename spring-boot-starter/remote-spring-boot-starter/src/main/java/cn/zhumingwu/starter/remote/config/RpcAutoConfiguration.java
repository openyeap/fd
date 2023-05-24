package cn.zhumingwu.starter.remote.config;

import cn.zhumingwu.starter.remote.properties.RpcProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.util.NamingUtils;
import cn.zhumingwu.starter.remote.RpcApiBeanDefinitionRegistry;
import cn.zhumingwu.starter.remote.RpcService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@EnableConfigurationProperties({RpcProperties.class})
public class RpcAutoConfiguration {
    static final String DEFAULT_RPC_SERVICE_NAME = "defaultRpcService";

    @Bean
    public RpcApiBeanDefinitionRegistry rpcApiBeanDefinitionRegistry() {
        NamingUtils.formatLog(log,"Rpc Api Bean Definition Registry Started");
        return new RpcApiBeanDefinitionRegistry();
    }

    @Bean(name = "/hessian/rpc/service")
    public HessianServiceExporter hessianServiceExporter() {
        NamingUtils.formatLog(log,"HessianServiceExporter Started");
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(defaultRpcService());

        exporter.setServiceInterface(RpcService.class);
        return exporter;
    }

    @Bean
    public RmiServiceExporter rmiServiceExporter() {
        NamingUtils.formatLog(log,"RmiServiceExporter Started");
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setService(defaultRpcService());
        exporter.setServiceName("rpcService");
        exporter.setServiceInterface(RpcService.class);
        exporter.setRegistryPort(1099);
        return exporter;
    }


    private RpcService defaultRpcService() {
        return new RpcService() {
            @Override
            public <T> Object invoke(Class<T> requiredType, String methodName, Object... args) {
                Object bean = ApplicationContextHolder.getBean(requiredType);

                if (bean == null) {
                    log.warn("No required Type: {} in current application context", requiredType);
                    return null;
                }
                try {
                    if (args.length > 0) {
                        Class<?>[] paramTypes = Arrays.stream(args).map(o -> o.getClass()).collect(Collectors.toSet()).toArray(new Class<?>[0]);
                        var method = bean.getClass().getMethod(methodName, paramTypes);
                        return method.invoke(bean, args);
                    } else {
                        return bean.getClass().getMethod(methodName).invoke(bean);
                    }

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error("RpcServiceImpl.invoke", e);
                }
                return null;
            }
        };
    }

    //
//    @Bean("httpInvokerProxyFactoryBean")
//    public HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean() {
//        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
//        factoryBean.setHttpInvokerRequestExecutor(new HttpComponentsHttpInvokerRequestExecutor());
//        factoryBean.setServiceUrl("http://127.0.0.1:8081/remoting/AccountService");
//        factoryBean.setServiceInterface(RpcService.class);
//        return factoryBean;
//    }


}