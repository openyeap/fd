package cn.zhumingwu.server.service.impl;

import cn.zhumingwu.api.service.HiService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.util.NamingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
@Slf4j
public class HiServiceImpl implements HiService {
    @Value("${spring.application.name:default}")
    String applicationName; 

    @Override
    public String hi(String message) {

        var lock = ApplicationContextHolder
                .getLockManager("consul")
                .getLock("lockKey");
        if (lock.tryLock()) {
            try {
                //处理任务
            } catch (Exception ex) {

            } finally {
                lock.unlock();   //释放锁
            }
        } else {
            //如果不能获取锁，则直接做其他事情
        }
        for (var item : RequestContextHolder.getRequestAttributes().getAttributeNames(0)) {
            NamingUtils.formatLog(log,"0:{}", item);
        }
        for (var item : RequestContextHolder.getRequestAttributes().getAttributeNames(1)) {
            NamingUtils.formatLog(log,"1:{}", item);
        }
        var list = ApplicationContextHolder.getCacheManagers();
        for (var entry : list.entrySet()) {
            var cacheManager = entry.getValue();
            NamingUtils.formatLog(log,"++++++++++name:{}+++++++++++", entry.getKey());
            NamingUtils.formatLog(log,"++++++++++class:{}+++++++++++", cacheManager.getClass().getCanonicalName());
            for (var name : entry.getValue().getCacheNames()) {
                NamingUtils.formatLog(log,"++++++++++cache:{}+++++++++++", name);
            }
        }
        return "hi " + message + " from " + this.applicationName;
    }
}
