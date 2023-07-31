package cn.zhumingwu.dataswitch.admin.service.impl;


import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobLogRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobTaskRepository;
import cn.zhumingwu.dataswitch.core.job.enums.JobStatus;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.core.job.coordinator.Coordinator;
import cn.zhumingwu.dataswitch.core.job.model.CallbackParam;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CoordinatorImpl implements Coordinator {
    @Resource
    public JobLogRepository jobLogRepository;
    @Resource
    private JobInfoRepository jobInfoRepository;

    @Resource
    private JobTaskRepository jobTaskRepository;
    @Resource
    private CoordinatorContext context;


    @Override
    public Result<String> callback(List<CallbackParam> callbackParamList) {
        for (var item : callbackParamList) {
            var taskId = item.getTaskId();
            var jobTaskRepositoryById = this.jobTaskRepository.findById(taskId);
            if (jobTaskRepositoryById.isPresent()) {
                var t = jobTaskRepositoryById.get();
                if (item.getStatus() == JobStatus.SUCCESS) {
                    t.setHandleCode(item.getCode());
                    t.setHandleTime(new Date(item.getTimestamp()));
                    t.setHandleMsg(item.getMessage());
                    t.setHandleStatus(item.getStatus());
                    this.jobTaskRepository.save(t);
                } else {
                    t.setHandleCode(item.getCode());
                    t.setHandleTime(new Date(item.getTimestamp()));
                    t.setHandleMsg(item.getMessage());
                    t.setHandleStatus(item.getStatus());
                    t.setExecutorFailRetryCount(t.getExecutorFailRetryCount() + 1);
                    this.jobTaskRepository.save(t);
                }
            }
        }
        return Result.success("");
    }
}
