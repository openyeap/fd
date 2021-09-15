package ltd.fdsa.switcher.core.pipeline.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.switcher.core.model.Record;
import ltd.fdsa.switcher.core.pipeline.Process;

import java.util.Map;

@Slf4j
public class DefaultPipeline extends AbstractPipeline implements Process {

    @Override
    public void collect(Record... records) {
        if (this.isRunning()) {
            for (var map : records) {
                StringBuilder sb = new StringBuilder();
                for (var entry : map.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append('=').append('"');
                    sb.append(entry.getValue());
                    sb.append('"');
                    sb.append(',').append(' ');
                }
                NamingUtils.formatLog(log, "{}:{}", this.name, sb.toString());
            }
        }
    }


}