package ltd.fdsa.switcher.core.pipeline.impl;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.switcher.core.pipeline.Channel;
import ltd.fdsa.switcher.core.pipeline.Pipeline;
import ltd.fdsa.switcher.core.config.Configuration;
import ltd.fdsa.web.view.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@Slf4j
public class DisruptorChannel extends AbstractPipeline implements Channel {
    private Disruptor<Map<String, Object>> disruptor;

    @Override
    public Result<String> init(Configuration configuration) {
        var result = super.init(configuration);
        if (result.getCode() == 200) {
            int bufferSize = this.config.getInt("bufferSize", 1024);
            // Construct the Disruptor
            this.disruptor = new Disruptor<Map<String, Object>>(HashMap::new, bufferSize, Executors.defaultThreadFactory());
            return Result.success();
        }
        return result;
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            // Connect the handler
            disruptor.handleEventsWith(new RecordHandler(this.nextSteps));
            // Start the Disruptor, starts all threads running
            disruptor.start();
        }
    }

    @Override
    public void collect(Map<String, Object>... records) {
        if (!this.isRunning()) {
            return;
        }

        //var ringBuffer = this.disruptor.getRingBuffer();
        //for (var record : records) {
        //    var sequence = ringBuffer.next();
        //    var re = ringBuffer.get(sequence);
        //    re.putAll(record);
        //    ringBuffer.publish(sequence);
        //}

        for (var record : records) {
            this.disruptor.publishEvent(new RecordSender(record));
        }
    }


    @AllArgsConstructor
    class RecordHandler implements EventHandler<Map<String, Object>> {
        private final List<Pipeline> sinks;

        @Override
        public void onEvent(Map<String, Object> stringObjectMap, long l, boolean b) throws Exception {
            for (var sink : sinks) {
                sink.collect(new HashMap<String, Object>(stringObjectMap));
            }
        }
    }

    @AllArgsConstructor
    class RecordSender implements EventTranslator<Map<String, Object>> {
        private Map<String, Object> record;

        @Override
        public void translateTo(Map<String, Object> stringObjectMap, long l) {
            stringObjectMap.putAll(record);
        }
    }
}
