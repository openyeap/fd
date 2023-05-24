package cn.zhumingwu.server.config;

import cn.zhumingwu.starter.remote.annotation.LoadBalanced;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.util.NamingUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;


@Configuration
@Slf4j
public class ApplicationConfig {

    /*
   Spring HTTP-invoker
    */
    @Bean(name = "/test")
    public HttpRequestExporter invokerExporter() {
        HttpRequestExporter exporter = new HttpRequestExporter();
        return exporter;
    }

    @Bean  // 将getRestTemplate修饰为Bean，交给spring管理
    @LoadBalanced  // 这里使用刚刚自定义的注解
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean  // 将getRestTemplate修饰为Bean，交给spring管理
    @LoadBalanced  // 这里使用刚刚自定义的注解
    public RestTemplate getRestTemplate2() {
        return new RestTemplate();
    }


    @Autowired(required = false)// 非必须的，将那些被@MyLoadBalanced注解修饰过的对象，自动装配到tpls集合中
    private List<RestTemplate> restTemplateList = Collections.emptyList();

    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        EhCacheCacheManager cacheManager = new EhCacheCacheManager(cacheManagerFactoryBean.getObject());
        return cacheManager;
    }
    
    @Bean
    public SmartInitializingSingleton loadBalanceInitializing() {
        return new SmartInitializingSingleton() {
            @Override
            public void afterSingletonsInstantiated() {
                NamingUtils.formatLog(log,"RestTemplate集合大小：{}", restTemplateList.size());
                for (RestTemplate rtl : restTemplateList) {
                    // 为了防止覆盖默认拦截器，将默认拦截器取出
                    List<ClientHttpRequestInterceptor> interceptors = rtl.getInterceptors();
                    // 将自定义的拦截器加入到默认拦截器中
                    interceptors.add(new MyClientHttpRequestInterceptor());
                    // 给RestTemplate设置拦截器
                    rtl.setInterceptors(interceptors);
                }
            }
        };
    }


//    /**
//     * 混合缓存管理
//     *
//     * @return cacheManager
//     */
//    @Bean
//    public CacheManager compositeCacheManager() {
//        var caffeineCacheManager = new CaffeineCacheManager("Caffeine");
//        var ehCacheCacheManager = new EhCacheCacheManager();
//        var myCacheManager = new HashMapCacheManager();
//        CompositeCacheManager cacheManager = new CompositeCacheManager(caffeineCacheManager, ehCacheCacheManager, myCacheManager);
//        cacheManager.setFallbackToNoOpCache(true);
//        cacheManager.afterPropertiesSet();
//        return cacheManager;
//    }

//    @Bean
//    public config cacheConfig() {
//        return new config();
//    }
//
//    class config implements SmartInitializingSingleton, BeanFactoryPostProcessor {
//
//        ConfigurableListableBeanFactory beanFactory;
//
//        @Override
//        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//            this.beanFactory = beanFactory;
//        }
//
//        @Override
//        public void afterSingletonsInstantiated() {
////            NamingUtils.formatLog(log,"cacheManagerList：{}", cacheManagerList.size());
////            CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
////            compositeCacheManager.setCacheManagers(cacheManagerList);
////            this.beanFactory.registerSingleton("compositeCacheManager", compositeCacheManager);
//
//            NamingUtils.formatLog(log,"RestTemplate集合大小：{}", restTemplateList.size());
//            for (RestTemplate rtl : restTemplateList) {
//                // 为了防止覆盖默认拦截器，将默认拦截器取出
//                List<ClientHttpRequestInterceptor> interceptors = rtl.getInterceptors();
//                // 将自定义的拦截器加入到默认拦截器中
//                interceptors.add(new MyClientHttpRequestInterceptor());
//                // 给RestTemplate设置拦截器
//                rtl.setInterceptors(interceptors);
//            }
//        }
//    }
}
