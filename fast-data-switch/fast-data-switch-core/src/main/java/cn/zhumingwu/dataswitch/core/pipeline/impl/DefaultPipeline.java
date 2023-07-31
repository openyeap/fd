package cn.zhumingwu.dataswitch.core.pipeline.impl;

import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Process;

@Slf4j
public class DefaultPipeline implements Process {

    @Override
    public void execute(Record... records) {
        if (this.isRunning()) {
            for (var map : records) {
                StringBuilder sb = new StringBuilder();
                for (var entry : map.toNormalMap().entrySet()) {
                    sb.append(entry.getKey());
                    sb.append('=').append('"');
                    sb.append(entry.getValue());
                    sb.append('"');
                    sb.append(',').append(' ');
                }
                log.info("{}:{}", this.toString(), sb.toString());
            }
        }
    }
}