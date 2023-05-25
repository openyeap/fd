package cn.zhumingwu.dataswitch.core.pipeline.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Channel;

@Slf4j
public class DirectChannel implements Channel {

    @Override
    public void execute(Record... records) {
        if (!this.isRunning()) {
            return;
        }
        for (var item : this.nextSteps()) {
            item.execute(records);
        }
    }
}