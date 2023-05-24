package cn.zhumingwu.base.refresh;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshScope implements Scope {

    private ConcurrentHashMap map = new ConcurrentHashMap();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (map.containsKey(name)) {
            return map.get(name);
        }

        //获取到实例
        Object object = objectFactory.getObject();
        map.put(name, object);
        return object;
    }

    @Override
    public Object remove(String name) {
        return map.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable runnable) {

    }

    @Override
    public Object resolveContextualObject(String name) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
