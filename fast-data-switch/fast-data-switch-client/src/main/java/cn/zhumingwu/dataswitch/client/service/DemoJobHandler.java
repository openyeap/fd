package cn.zhumingwu.dataswitch.client.service;

import cn.zhumingwu.dataswitch.core.job.coordinator.Coordinator;
import cn.zhumingwu.dataswitch.core.job.executor.JobContext;
import cn.zhumingwu.dataswitch.core.job.handler.IJobHandler;
import cn.zhumingwu.starter.remote.annotation.RpcClient;
import org.springframework.stereotype.Service;

@Service
public class DemoJobHandler extends IJobHandler {

    @RpcClient(service = "JobCoordinator")
    Coordinator coordinator;


    @Override
    public void execute() throws Exception {
        JobContext.getJobContext().log("{}{}", "test", System.currentTimeMillis());
        this.coordinator.callback(null);
    }
}
