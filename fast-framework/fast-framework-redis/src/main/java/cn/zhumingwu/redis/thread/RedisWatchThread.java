package cn.zhumingwu.redis.thread;

import cn.zhumingwu.redis.properties.RedisConfigProperties;
import cn.zhumingwu.redis.register.NewService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.event.RefreshedEvent;
import cn.zhumingwu.base.event.RemotingEvent;
import cn.zhumingwu.base.event.ServiceDiscoveredEvent;
import cn.zhumingwu.base.service.ServiceInfo;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.TaskScheduler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
public class RedisWatchThread implements SmartLifecycle {

    private final RedisConfigProperties properties;

    private final TaskScheduler taskScheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
    private final RedisTemplate<String, NewService> serviceRedisTemplate = new RedisTemplate<>();
    private final RedisTemplate<String, RemotingEvent> eventRedisTemplate = new RedisTemplate<>();
    private final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

    private ScheduledFuture<?> serviceRegisterFuture;

    public RedisWatchThread(RedisConfigProperties properties, TaskScheduler taskScheduler, RedisConnectionFactory connectionFactory) {
        this.properties = properties;
        this.taskScheduler = taskScheduler;
        this.redisConnectionFactory = connectionFactory;
        this.serviceRedisTemplate.setConnectionFactory(this.redisConnectionFactory);
        this.stringRedisTemplate.setConnectionFactory(this.redisConnectionFactory);
        this.eventRedisTemplate.setConnectionFactory(this.redisConnectionFactory);
        this.redisMessageListenerContainer.setConnectionFactory(this.redisConnectionFactory);
        this.redisMessageListenerContainer.setMaxSubscriptionRegistrationWaitingTime(this.properties.getWaitTime().toMillis());
        // service watch
        if (this.properties.getServiceWatch().isEnabled()) {
            redisMessageListenerContainer.addMessageListener(
                    new MessageListener() {
                        @Override
                        public void onMessage(Message message, byte[] bytes) {
                            log.info("channel:{},body:{}", message.getChannel(), message.getBody());
                            serviceWatch();
                        }
                    }, new PatternTopic(this.properties.getServiceWatch().getKeyPrefix()));

        }
        // event watch
        if (this.properties.getEventWatch().isEnabled()) {
            redisMessageListenerContainer.addMessageListener(
                    new MessageListener() {
                        @Override
                        public void onMessage(Message message, byte[] bytes) {
                            log.info("channel:{},body:{}", message.getChannel(), message.getBody());
                            var event = (RemotingEvent) serviceRedisTemplate.getValueSerializer().deserialize(message.getBody());
                            if (event != null) {
                                ApplicationContextHolder.publishLocal(event.getTarget());
                            }
                        }
                    }, new PatternTopic(this.properties.getEventWatch().getKeyPrefix()));
        }
        // config watch
        if (this.properties.getConfigWatch().isEnabled()) {
            redisMessageListenerContainer.addMessageListener(
                    new MessageListener() {
                        @Override
                        public void onMessage(Message message, byte[] bytes) {
                            log.info("channel:{},body:{}", message.getChannel(), message.getBody());
                            configWatch();
                        }
                    }, new PatternTopic(this.properties.getConfigWatch().getKeyPrefix()));
        }
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            if (this.properties.isEnabled()) {
                this.serviceRegisterFuture = this.taskScheduler.scheduleWithFixedDelay(this::serviceRegister, this.properties.getDelay());
            }
        }
    }

    public void serviceWatch() {
        Map<String, List<ServiceInfo>> data = new LinkedHashMap<>();
        for (var key : this.serviceRedisTemplate.keys(this.properties.getServiceWatch().getKeyPrefix())) {
            var services = this.serviceRedisTemplate.opsForSet().members(key).stream().map(m -> {
                return ServiceInfo.builder().ip(m.getHost()).port(m.getPort()).build();
            }).collect(Collectors.toList());
            data.put(key.substring(this.properties.getServiceWatch().getKeyPrefix().length() - 1), services);
        }
        ApplicationContextHolder.publishLocal(new ServiceDiscoveredEvent(this, data));
    }

    public void configWatch() {
        Map<String, String> data = new LinkedHashMap<>();
        var prefix = this.properties.getConfigWatch().getKeyPrefix() + ":" + this.properties.getName();
        for (var key : this.stringRedisTemplate.keys(prefix)) {
            var value = this.stringRedisTemplate.opsForValue().get(key);
            data.put(key.substring(prefix.length() - 1), value);
        }
        ApplicationContextHolder.publishLocal(new RefreshedEvent(this, data));
    }

    public void serviceRegister() {
        //判断服务是否已经启动
        if (!this.running.get()) {
            return;
        }
        try {
            NewService register = new NewService(this.properties);
            serviceRedisTemplate.convertAndSend(this.properties.getServiceWatch().getKeyPrefix(), register);
            serviceRedisTemplate.opsForSet().add(this.properties.getServiceWatch().getKeyPrefix() + ":" + register.getName(), register);
        } catch (Exception e) {
            log.error("Error Consul register", e);
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            if (this.serviceRegisterFuture != null) {
                this.serviceRegisterFuture.cancel(true);
            }
            redisMessageListenerContainer.stop();
        }
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    public RedisMessageListenerContainer getWatcher() {
        return this.redisMessageListenerContainer;
    }
}

