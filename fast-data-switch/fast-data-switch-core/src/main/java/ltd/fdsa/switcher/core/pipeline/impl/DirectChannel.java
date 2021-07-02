package ltd.fdsa.switcher.core.pipeline.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.switcher.core.pipeline.Channel;

import java.util.Map;

@Slf4j
public class DirectChannel extends AbstractPipeline implements Channel {


    @Override
    public void collect(Map<String, Object>... records) {
        if (!this.isRunning()) {
            return;
        }
        for (var item : this.nextSteps) {
            item.collect(records);
        }
    }
}