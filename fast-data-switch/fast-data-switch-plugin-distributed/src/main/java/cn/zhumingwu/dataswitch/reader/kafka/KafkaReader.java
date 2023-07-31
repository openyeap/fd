package cn.zhumingwu.dataswitch.reader.kafka;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.dataswitch.core.model.Column;
import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Reader;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

@Slf4j
public class KafkaReader implements Reader {
    static final String TOPICS_CONFIG = "topics";
    Collection<String> topics;
    KafkaConsumer<String, String> kafkaConsumer;
    Duration duration;

    @Override
    public void init() {
        this.topics = Arrays.asList(this.context().get(TOPICS_CONFIG).split(","));
        this.duration = Duration.ofMillis(this.context().getLong(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 100));
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.context().get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        //kafka地址，多个地址用逗号分割
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.context().get(ConsumerConfig.GROUP_ID_CONFIG));
        this.kafkaConsumer = new KafkaConsumer<String, String>(properties);
    }

    @Override
    public void execute(Record... records) {
        if (this.isRunning()) {
            for (var item : this.nextSteps()) {
                item.execute(records);
            }
        }
    }

    @Override
    public void start() {
        this.kafkaConsumer.subscribe(this.topics);// 订阅消息
        while (this.isRunning()) {
            this.kafkaConsumer
                    .poll(this.duration)
                    .forEach(record -> {
                        var key = record.key();
                        var value = record.value();
                        var item = new Record();
                        if (!Strings.isNullOrEmpty(key)) {
                            item.add(new Column("key", key));
                        }
                        if (!Strings.isNullOrEmpty(value)) {
                            item.add(new Column("value", value));
                        }
                        this.execute(item);
                    });
        }
    }

    @Override
    public void stop() {
        this.kafkaConsumer.unsubscribe();
    }
}
