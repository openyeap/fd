package ltd.fdsa.switcher.core.pipeline.impl;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.switcher.core.model.Record;
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
    private Disruptor<Record> disruptor;

    @Override
    public Result<String> init(Configuration configuration) {
        var result = super.init(configuration);
        if (result.getCode() == 200) {
            int bufferSize = this.config.getInt("bufferSize", 1024);
            // Construct the Disruptor
            this.disruptor = new Disruptor<Record>(Record::new, bufferSize, Executors.defaultThreadFactory());
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
    public void collect(Record... records) {
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
    class RecordHandler implements EventHandler<Record> {
        private final List<Pipeline> sinks;

        @Override
        public void onEvent(Record record, long l, boolean b) throws Exception {
            for (var sink : sinks) {
                sink.collect(record);
            }
        }
    }

    @AllArgsConstructor
    class RecordSender implements EventTranslator<Record> {
        private Record record;

        @Override
        public void translateTo(Record record, long l) {
        }
    }
}
