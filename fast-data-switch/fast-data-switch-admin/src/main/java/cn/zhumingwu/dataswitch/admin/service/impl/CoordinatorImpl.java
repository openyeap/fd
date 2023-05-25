package cn.zhumingwu.dataswitch.admin.service.impl;


import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobLogRepository;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.core.job.coordinator.Coordinator;
import cn.zhumingwu.dataswitch.core.job.model.CallbackParam;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CoordinatorImpl implements Coordinator {
    @Resource
    public JobLogRepository jobLogRepository;
    @Resource
    private JobInfoRepository JobInfoDao;

    @Resource
    private JobGroupRepository JobGroupDao;
    @Resource
    private CoordinatorContext context;



    @Override
    public Result<String> callback(List<CallbackParam> callbackParamList) {
        return null;
    }
}
