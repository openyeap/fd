package cn.zhumingwu.dataswitch.admin.thread;

import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobRegistryRepository;
import cn.zhumingwu.dataswitch.admin.service.impl.JobService;
import lombok.var;
import cn.zhumingwu.dataswitch.admin.entity.JobGroup;
import cn.zhumingwu.dataswitch.core.job.enums.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * job registry instance
 */
public class JobRegistryMonitorHelper {
    private static Logger logger = LoggerFactory.getLogger(JobRegistryMonitorHelper.class);

    private static JobRegistryMonitorHelper instance = new JobRegistryMonitorHelper();
    private Thread registryThread;
    private volatile boolean toStop = false;

    public static JobRegistryMonitorHelper getInstance() {
        return instance;
    }

    public void start() {
        registryThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                while (!toStop) {
                                    try {
                                        // auto registry group
                                        List<JobGroup> groupList =
                                                ApplicationContextHolder.getBean(JobService.class).findByAddressList(0);
                                        if (groupList != null && !groupList.isEmpty()) {

                                            // remove dead address (admin/executor)
                                            List<Integer> ids =
                                                    ApplicationContextHolder.getBean(JobService.class)
                                                            .findDead(RegistryConfig.DEAD_TIMEOUT, new Date());
                                            if (ids != null && ids.size() > 0) {
                                                var dao = ApplicationContextHolder.getBean(JobRegistryRepository.class);
                                                for (var i : ids) {
                                                    dao.deleteById(i);
                                                }
                                            }

                                            // fresh online address (admin/executor)
                                            HashMap<String, List<String>> appAddressMap =
                                                    new HashMap<String, List<String>>();
                                            List<JobRegistry> list =
                                                    ApplicationContextHolder.getBean(JobService.class)
                                                            .findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
                                            if (list != null) {
                                                for (JobRegistry item : list) {
                                                    if (RegistryConfig.EXECUTOR
                                                            .name()
                                                            .equals(item.getRegistryGroup())) {
                                                        String appName = item.getRegistryKey();
                                                        List<String> registryList = appAddressMap.get(appName);
                                                        if (registryList == null) {
                                                            registryList = new ArrayList<String>();
                                                        }

                                                        if (!registryList.contains(item.getRegistryValue())) {
                                                            registryList.add(item.getRegistryValue());
                                                        }
                                                        appAddressMap.put(appName, registryList);
                                                    }
                                                }
                                            }

                                            // fresh group address
                                            for (JobGroup group : groupList) {
                                                List<String> registryList = appAddressMap.get(group.getName());
                                                String addressListStr = null;
                                                if (registryList != null && !registryList.isEmpty()) {
                                                    Collections.sort(registryList);
                                                    addressListStr = "";
                                                    for (String item : registryList) {
                                                        addressListStr += item + ",";
                                                    }
                                                    addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
                                                }
                                                group.setAddressList(addressListStr);
                                                ApplicationContextHolder.getBean(JobGroupRepository.class).save(group);
                                            }
                                        }
                                    } catch (Exception e) {
                                        if (!toStop) {
                                        }
                                    }
                                    try {
                                        TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                                    } catch (InterruptedException e) {
                                        if (!toStop) {
                                        }
                                    }
                                }
                            }
                        });
        registryThread.setDaemon(true);
        registryThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        registryThread.interrupt();
        try {
            registryThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
