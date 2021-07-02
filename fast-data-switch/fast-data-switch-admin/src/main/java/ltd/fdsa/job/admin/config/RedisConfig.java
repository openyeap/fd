package ltd.fdsa.job.admin.config;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.util.NamingUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("mytopic"));
        return container;
    }

    /**
     * 绑定消息监听者和接收监听的方法,必须要注入这个监听器，不然会报错
     */
    @Bean
    public MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(new Receiver(), "receiveMessage");
    }

    @Slf4j
    public class Receiver {

        public void receiveMessage(String message) {
            NamingUtils.formatLog(log,"Received <" + message + ">");
        }
    }
}
