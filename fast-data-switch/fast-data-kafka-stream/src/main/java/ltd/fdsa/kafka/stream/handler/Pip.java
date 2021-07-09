package ltd.fdsa.kafka.stream.handler;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Pip {

    public static void main(String[] args){

        Properties props = new Properties();
        //程序的唯一标识符以区别于其他应用程序与同一Kafka集群通信
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        //用于建立与Kafka集群的初始连接的主机/端口对的列表
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.2.38:9092");
        //记录键值对的默认序列化和反序列化库
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //定义Streams应用程序的计算逻辑,计算逻辑被定义为topology连接的处理器节点之一
        final StreamsBuilder builder = new StreamsBuilder();
        //将"my-replicated-topic写入另一个Kafka toptic(skindow-toptic)
        builder.stream("20210331174146_pg77JRY_t_sample2021033117391553").to("skindow-toptic");
        //构建Topology对象
        final Topology topology = builder.build();
        //构建 kafka流 API实例
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // 附加关闭处理程序来捕获control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);//是非正常退出，就是说无论程序正在执行与否，都退出
        }
        System.exit(0);//正常退出，程序正常执行结束退出

    }
}
