package cn.zhumingwu.dataswitch.core.job.handler.impl;

import lombok.var;
import cn.zhumingwu.dataswitch.core.util.JobFileAppender;
import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Process;
import cn.zhumingwu.dataswitch.core.util.ScriptUtil;

import java.io.IOException;
import java.util.Arrays;

public class ScriptJobHandler implements Process {


    @Override
    public void execute(Record... records) {
        Arrays.stream(records).map(m -> m.toNormalMap()).forEach(context -> {

            // log file
            String logFileName = JobFileAppender.contextHolder.get();
            var cmd = context.get("cmd").toString();
            var scriptFileName = context.get("scriptFileName").toString();
            var scriptParams = context.get("scriptParams").toString();

            try {
                ScriptUtil.execToFile(cmd, scriptFileName, logFileName, scriptParams);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
