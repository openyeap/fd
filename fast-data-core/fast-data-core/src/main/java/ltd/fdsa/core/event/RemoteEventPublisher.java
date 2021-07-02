package ltd.fdsa.core.event;

public interface RemoteEventPublisher {
    void send(RemotingEvent event);
}

