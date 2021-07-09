package ltd.fdsa.kafka.stream.handler;


import org.apache.catalina.Store;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WordCountTopologyDemo {

    public static void main(String[] args) {
        //0.配置KafkaStreams的连接信息
        Properties props = new Properties();
        //stream 实例名
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-low-level");
        //kafka 服务地址
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //配置默认的key序列化和反序列化
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StoreBuilder<KeyValueStore<String, Integer>> keyValueStoreStoreBuilder = Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore("storeName"), Serdes.String(), Serdes.Integer());

        StreamsBuilder builder = new StreamsBuilder();
        builder.addStateStore(keyValueStoreStoreBuilder);
        //1.定义计算拓扑
        Topology topology = builder.build();
        topology.addSource("s1", "topic01");
        topology.addProcessor("p1", () -> new WordCountProcessor(), "s1");
        topology.addStateStore(keyValueStoreStoreBuilder, "p1");
        topology.addSink("sk1", "topic02", new StringSerializer(), new IntegerSerializer(), "p1");

        //3.创建KafkaStreams
        KafkaStreams kafkaStreams = new KafkaStreams(topology, props);
        //4.启动计算
        kafkaStreams.start();
    }
}
