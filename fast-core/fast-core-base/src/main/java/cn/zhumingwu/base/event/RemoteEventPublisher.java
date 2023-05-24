package cn.zhumingwu.base.event;

public interface RemoteEventPublisher {
    void send(RemotingEvent event);
}

