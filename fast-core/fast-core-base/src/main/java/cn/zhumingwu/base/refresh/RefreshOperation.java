package cn.zhumingwu.base.refresh;


import cn.zhumingwu.base.event.RefreshedEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshOperation {

    BeanDefinitionRegistry beanDefinitionRegistry;
    private ConcurrentHashMap map = new ConcurrentHashMap();
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        RefreshScopeRegistry refreshScopeRegistry = (RefreshScopeRegistry) applicationContext.getBean(RefreshScopeRegistry.NAME);
        beanDefinitionRegistry = refreshScopeRegistry.getBeanDefinitionRegistry();
        MutablePropertySources mutablePropertySources = applicationContext.getEnvironment().getPropertySources();
        OriginTrackedMapPropertySource originTrackedMapPropertySource = new OriginTrackedMapPropertySource(RefreshScopeRegistry.NAME, map);
        mutablePropertySources.addFirst(originTrackedMapPropertySource);
    }

    public void refresh() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            if (RefreshScopeRegistry.NAME.equalsIgnoreCase(beanDefinition.getScope())) {
                applicationContext.getBeanFactory().destroyScopedBean(beanDefinitionName);
                //再次实例化
                applicationContext.getBean(beanDefinitionName);
            }
        }
    }

    public void update(String key, String value) {
        MutablePropertySources mutablePropertySources = applicationContext.getEnvironment().getPropertySources();
        PropertySource propertySource = mutablePropertySources.get(RefreshScopeRegistry.NAME);
        ConcurrentHashMap collection = (ConcurrentHashMap) propertySource.getSource();
        collection.put(key, value);
    }

    /**
     * 较高优先处理配置信息变更
     *
     * @author zhumingwu
     *
     * @param refreshedEvent event
     */
    @EventListener
    @Order(-100)
    public void getRefreshedEvent(RefreshedEvent refreshedEvent) {
        for (var item : refreshedEvent.getData().entrySet()) {
            update(item.getKey(), item.getValue());
        }
        // 更新数据后需要刷新到容器
        refresh();
    }
}
