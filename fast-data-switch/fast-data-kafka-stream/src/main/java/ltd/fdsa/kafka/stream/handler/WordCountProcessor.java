package ltd.fdsa.kafka.stream.handler;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.streams.processor.Cancellable;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class WordCountProcessor implements Processor<String, String, String, Integer> {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final HashMap<String, Integer> wordPair = new HashMap<>();

    private ProcessorContext<String, Integer> context;
    private Cancellable cancellable;
    private KeyValueStore<String, Integer> stateStore;

    @Override
    public void init(ProcessorContext<String, Integer> context) {
        if (this.running.compareAndSet(false, true)) {
            log.info("=={}.{}==", "WordCountProcessor", "init");
            this.context = context;
            //定时输出结果
            var duration = (Duration) this.context.appConfigs().get("window");
            if (duration == null) {
                duration = Duration.ofSeconds(30);
            }
            this.stateStore = this.context.getStateStore("storeName");
            this.cancellable = context.schedule(duration, PunctuationType.WALL_CLOCK_TIME, (ts) -> {
                for (var entry : wordPair.entrySet()) {
                    var record = new Record<String, Integer>(entry.getKey(), entry.getValue(), new Date().getTime());
                    this.context.forward(record);
                }
            });
        } else {
            log.warn("====={} {}======", "WordCountProcessor", "is working now!");
        }
    }


    @Override
    public void process(Record<String, String> record) {

        //判断服务是否运行中
        if (!this.running.get()) {
            return;
        }
        String[] words = record.value().split("\\W+");
        for (int i = 0; i < words.length; i++) {
            int count = 0;
            if (wordPair.containsKey(words[i])) {
                count = wordPair.get(words[i]);
            }
            count += 1;
            wordPair.put(words[i], count);
        }
    }

    @Override
    public void close() {
        if (this.running.compareAndSet(true, false)) {
            this.cancellable.cancel();
            //flush data before close
            for (var entry : wordPair.entrySet()) {
                var record = new Record<String, Integer>(entry.getKey(), entry.getValue(), new Date().getTime());
                this.context.forward(record);
            }
        }
    }
}
