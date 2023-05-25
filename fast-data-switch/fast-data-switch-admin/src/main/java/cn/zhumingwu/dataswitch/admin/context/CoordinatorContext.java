package cn.zhumingwu.dataswitch.admin.context;

import com.caucho.hessian.client.HessianProxyFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import cn.zhumingwu.dataswitch.core.model.NewService;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class CoordinatorContext {
    private static HashMap<String, NewService> serviceMap = new HashMap<>();
    private static HashMap<String, List<Executor>> executorMap = new HashMap<>();
    private static Map<String, Executor> cache = new HashMap<>();

    private final HessianProxyFactory factory = new HessianProxyFactory();
    Lock lock = new ReentrantLock();

    public NewService registry(NewService newService) {
        lock.lock();
        try {
            var result = serviceMap.put(newService.getId(), newService);
            refresh();
            return result;
        } catch (MalformedURLException e) {
            log.error("", e);
            return null;
        } finally {
            lock.unlock();
        }
    }

    public NewService unRegistry(NewService newService) {
        lock.lock();
        try {
            var result = serviceMap.remove(newService.getId());
            refresh();
            return result;
        } catch (MalformedURLException e) {
            log.error("", e);
            return null;
        } finally {
            lock.unlock();
        }
    }

    void refresh() throws MalformedURLException {
        executorMap.clear();
        for (var entry : serviceMap.entrySet()) {
            var url = entry.getValue().getUrl();
            for (var process : entry.getValue().getHandles().entrySet()) {
                List<Executor> list;
                if (!executorMap.containsKey(process.getKey())) {
                    list = new ArrayList<>();
                    executorMap.put(process.getKey(), list);
                } else {
                    list = executorMap.get(process.getKey());
                }
                var executor = cache.get(url);
                if (executor == null) {
                    executor = (Executor) factory.create(Executor.class, url);
                    cache.put(url, executor);
                }
                list.add(executor);
            }
        }
    }

    public List<Executor> getExecutorsByProcess(String process) {
        lock.lock();
        try {
            return executorMap.get(process);
        } finally {
            lock.unlock();
        }
    }
}
