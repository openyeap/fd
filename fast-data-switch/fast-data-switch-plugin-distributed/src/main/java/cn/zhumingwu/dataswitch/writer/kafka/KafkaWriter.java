package cn.zhumingwu.dataswitch.writer.kafka;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Writer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

@Slf4j
public class KafkaWriter implements Writer {
    static final String TOPICS_CONFIG = "topics";
    Collection<String> topics;
    KafkaProducer<String, String> kafkaProducer;

    @Override
    public void init() {
        this.topics = Arrays.asList(this.context().get(TOPICS_CONFIG).split(","));
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.context().get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.kafkaProducer = new KafkaProducer<String, String>(properties);
    }

    @Override
    public void execute(Record... records) {
        if (!this.isRunning()) {
            return;
        }
        for (var item : records) {
            this.topics.stream()
                    .map(topic -> new ProducerRecord<String, String>(topic, item.toString()))
                    .forEach(record -> {
                        kafkaProducer.send(record);
                    });
        }
        for (var item : this.nextSteps()) {
            item.execute(records);
        }
    }
}
