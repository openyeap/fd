package cn.zhumingwu.core.event;

public interface RemoteEventPublisher {
    void send(RemotingEvent event);
}

