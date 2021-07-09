package ltd.fdsa.kafka.stream.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;


import org.springframework.stereotype.Service;




@JobHandler("printHandler")
@Service
public class PrintHandler extends IJobHandler {

    KafkaStreams streams;

    @Override
    public void init(){

        System.out.println("开启=================================");
        Properties props = new Properties();
        //程序的唯一标识符以区别于其他应用程序与同一Kafka集群通信
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        //用于建立与Kafka集群的初始连接的主机/端口对的列表
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //记录键值对的默认序列化和反序列化库
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //定义Streams应用程序的计算逻辑,计算逻辑被定义为topology连接的处理器节点之一
        StreamsBuilder builder = new StreamsBuilder();
        //将"my-replicated-topic写入另一个Kafka toptic(skindow-toptic)
        builder.stream("topic-input").to("topic-output");

        //构建Topology对象
        Topology topology = builder.build();
        //构建 kafka流 API实例
        streams = new KafkaStreams(topology, props);


    }

//    @Override
    public void destroy() {
        System.out.println("关闭=================================");
        super.destroy();
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //构建Topology对象
        streams.start();
        return ReturnT.SUCCESS;
    }


}
