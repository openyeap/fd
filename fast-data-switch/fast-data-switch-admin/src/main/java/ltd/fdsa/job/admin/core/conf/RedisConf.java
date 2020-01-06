package ltd.fdsa.job.admin.core.conf;
 
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import ltd.fdsa.job.admin.Receiver;
 
@Configuration
public class RedisConf {
	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
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
}