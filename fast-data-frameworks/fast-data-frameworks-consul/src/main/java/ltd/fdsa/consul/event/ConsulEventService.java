package ltd.fdsa.consul.event;


import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.event.RemoteEventPublisher;
import ltd.fdsa.core.event.RemotingEvent;

@Slf4j
public class ConsulEventService implements RemoteEventPublisher {

    private final ConsulClient consulClient;

    public ConsulEventService(ConsulClient consulClient) {
        this.consulClient = consulClient;
    }

    @Override
    public void send(RemotingEvent event) {
        this.consulClient.eventFire(event.getName(), event.getPayload(), null, QueryParams.DEFAULT);
    }
}

