package cn.zhumingwu.redis.event;


import cn.zhumingwu.redis.properties.RedisConfigProperties;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.core.event.RemoteEventPublisher;
import cn.zhumingwu.core.event.RemotingEvent;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisEventService implements RemoteEventPublisher {

    private final RedisConfigProperties properties;
    private final RedisTemplate<String, RemotingEvent> eventRedisTemplate;

    public RedisEventService(RedisConfigProperties properties, RedisConnectionFactory connectionFactory) {
        this.properties = properties;
        this.eventRedisTemplate = new RedisTemplate<String, RemotingEvent>();
        this.eventRedisTemplate.setConnectionFactory(connectionFactory);
    }

    @Override
    public void send(RemotingEvent event) {
        this.eventRedisTemplate.convertAndSend(this.properties.getEventWatch().getKeyPrefix(), event);
    }
}

