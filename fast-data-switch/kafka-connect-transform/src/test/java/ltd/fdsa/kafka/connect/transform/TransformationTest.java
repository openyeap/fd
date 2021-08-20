/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ltd.fdsa.kafka.connect.transform;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.junit.Before;
import org.junit.Test;


import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
public class TransformationTest {


    Transformation<SinkRecord> transformation;

    @Before
    public void before() {

    }

    @Test
    public void testSend() {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.168.4.131:9092");
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class.getName());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class.getName());
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //生产者发送消息
        String topic = "connect-offsets";

// 3.写回新
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        var result = producer.send(new ProducerRecord<>(topic,
                "[\"mysql_source\",{\"protocol\":\"1\",\"table\":\"dev.t_person\"}]",
                "{\"incrementing\":0}"));

//列出topic的相关信息
        List<PartitionInfo> partitions = new ArrayList<PartitionInfo>();
        partitions = producer.partitionsFor(topic);
        for (PartitionInfo p : partitions) {
            System.out.println(p);
        }

        System.out.println("send message over.");
        producer.close(100, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testKafka() {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.168.4.131:9092");

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "connect-offsets");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.BytesDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.BytesDeserializer");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList("connect-offsets"));

        //kafka的分区逻辑是在poll方法里执行的,所以执行seek方法之前先执行一次poll方法
        //获取当前消费者消费分区的情况
        Set<TopicPartition> assignment = new HashSet<>();
        while (assignment.size() == 0) {
            //如果没有分配到分区,就一直循环下去
            consumer.poll(Duration.ofMillis(100L));
            assignment = consumer.assignment();
        }
        for (TopicPartition tp : assignment) {
            //消费第当前分区的offset为10的消息
            consumer.seek(tp, 0);
        }

//
//        for (var partition : consumer.partitionsFor("connect-offsets")) {
//            consumer.assign(Arrays.asList(new TopicPartition(partition.topic(), partition.partition())));
//            consumer.seekToBeginning(Arrays.asList(new TopicPartition(partition.topic(), partition.partition())));
//        }

        while (true) {
            for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100))) {
                System.out.printf("%s\n", record);
                // 处理消息的逻辑省略
            }

            try {
                // poll 的数据全部处理完提交
//                consumer.commitSync();
            } catch (CommitFailedException e) {
                log.error("commit failed", e);
            }
        }
//        consumer.commitSync();


//        AdminClient kafkaAdminClient = KafkaAdminClient.create(properties);
//        // 3.执行删除
//        DeleteRecordsResult result = kafkaAdminClient.deleteRecords(recordsToDelete);
//        Map<TopicPartition, KafkaFuture<DeletedRecords>> lowWatermarks = result.lowWatermarks();
//        try {
//            for (Map.Entry<TopicPartition, KafkaFuture<DeletedRecords>> entry : lowWatermarks.entrySet()) {
//                System.out.println(entry.getKey().topic() + " " + entry.getKey().partition() + " " + entry.getValue().get().lowWatermark());
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        kafkaAdminClient.close();
    }

    private void removeLatest(String topic, Long offsetFrom, Long offsetTo, Properties properties) {
// 1.获取老数据
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        for (var partition : consumer.partitionsFor(topic)) {
            consumer.assign(Arrays.asList(new TopicPartition(partition.topic(), partition.partition())));
            consumer.seek(new TopicPartition(partition.topic(), partition.partition()), offsetFrom);
        }
        List<ConsumerRecord<String, String>> list = new LinkedList<>();
        var stop = false;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                if (record.offset() > offsetTo) {
                    stop = true;
                    break;
                }
                list.add(record);
                // 处理消息的逻辑省略
                // System.out.printf("%s\n", record);
            }
            if (stop) {
                break;
            }
//            try {
//                 consumer.commitSync();
//            } catch (CommitFailedException e) {
//                log.error("commit failed", e);
//            }
        }

        // 2.执行删除
        AdminClient kafkaAdminClient = KafkaAdminClient.create(properties);
        var result = kafkaAdminClient.deleteTopics(Arrays.asList(topic));

// 3.写回新
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (var record : list) {
            producer.send(new ProducerRecord<>(record.topic(), record.key(), record.value()));
        }
    }
}
