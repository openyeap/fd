//package cn.zhumingwu.dataswitch.channel.kafka;
//
//import lombok.extern.slf4j.Slf4j;
//import lombok.var;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.serialization.StringSerializer;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Properties;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.function.Function;
//
//@Slf4j
//public class KafkaClient {
//
//    private final static AtomicInteger next = new AtomicInteger(0);
//    public final String topic;
//    private final KafkaProducer<String, String> kafkaProducer;
//    private final KafkaConsumer<String, String> kafkaConsumer;
//
//    public KafkaClient(String topic, String... hosts) {
//        this.topic = topic;
//        Properties p = new Properties();
//        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", hosts));//kafka地址，多个地址用逗号分割
//        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        this.kafkaProducer = new KafkaProducer<String, String>(p);
//        p.put(ConsumerConfig.GROUP_ID_CONFIG, this.topic);
//        this.kafkaConsumer = new KafkaConsumer<String, String>(p);
//    }
//
//
//    public void send(String... messages) throws InterruptedException {
//        for (var msg : messages) {
//            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msg);
//            kafkaProducer.send(record);
//        }
//    }
//
//    public void start(Function<String, Boolean> callback) {
//
//        kafkaConsumer.subscribe(Collections.singletonList(this.topic));// 订阅消息
//
//        while (true) {
//            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
//            for (ConsumerRecord<String, String> record : records) {
//                callback.apply(record.value());
//            }
//        }
//    }
//}
