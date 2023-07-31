package cn.zhumingwu.dataswitch.admin.service.impl;


import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import cn.zhumingwu.dataswitch.core.job.model.LogResult;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
public class ExecutorClientImpl implements Executor {
    private final List<Executor> list;

    public ExecutorClientImpl(List<Executor> list) {
        this.list = list;
    }

    @Override
    public Result<String> start(TriggerParam param) {

        for (var item : list) {
            item.start(param);
        }

        return Result.success();
    }

    @Override
    public Result<String> stop(Long jobId, Long taskId) {
        return null;
    }

    @Override
    public Result<Map<String, String>> stat() {
        return null;
    }

    @Override
    public Result<Map<String, String>> stat(Long taskId) {
        return null;
    }

    @Override
    public Result<LogResult> stat(String handler, Long jobId, Long taskId, long timestamp, Long lastVersion) {
        return null;
    }
}
