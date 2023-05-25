package cn.zhumingwu.dataswitch.channel.kafka;

import cn.zhumingwu.dataswitch.channel.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Channel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Properties;

/*
使用Kafka实现数据渠道功能
*/
@Slf4j
public class KafkaChannel implements Channel {

    HttpClient httpClient = new HttpClient(null);
    String topic;
    KafkaProducer<String, String> kafkaProducer;


    @Override
    public void init() {
        this.topic  = "//todo 根据上下文名称" ;
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.context().get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.kafkaProducer = new KafkaProducer<String, String>(properties);

        // Send Job Config to Cluster
        var configurations = this.context().getConfigurations("pipelines");
        if (configurations != null && configurations.length > 0) {
            // invoke rpc to init
            httpClient.post("/api/job/init", RequestBody.create(MediaType.get("application/json"), this.context().toString()));
        }
        // Cluster will return related job Token
        // create clients to manager cluster nodes
        // then we can  process call remotely to collect data;
    }

    @Override
    public void execute(Record... records) {
        if (!this.isRunning()) {
            return;
        }

        Arrays.stream(records).map(item -> new ProducerRecord<String, String>(topic, item.toString()))
                .forEach(record -> {
                    this.kafkaProducer.send(record);
                });
     }

}
