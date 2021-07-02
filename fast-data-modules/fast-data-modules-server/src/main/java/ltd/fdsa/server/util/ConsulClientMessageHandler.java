//package ltd.fdsa.server.util;
//
//import com.ecwid.consul.v1.ConsulClient;
//import com.ecwid.consul.v1.QueryParams;
//import com.ecwid.consul.v1.Response;
//import com.ecwid.consul.v1.event.EventListRequest;
//import com.ecwid.consul.v1.event.model.Event;
//import com.ecwid.consul.v1.event.model.EventParams;
//import com.ecwid.consul.v1.kv.model.GetValue;
//import lombok.extern.slf4j.Slf4j;
//import lombok.var;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.handler.AbstractMessageHandler;
//import org.springframework.messaging.Message;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//import java.util.Map;
//
//@Configuration
//@Slf4j
//public class ConsulClientMessageHandler extends AbstractMessageHandler {
//    private final ConsulClient consulClient;
//
//    public ConsulClientMessageHandler(ConsulClient consul) {
//        this.consulClient = consul;
//    }
//
//    public ConsulClientMessageHandler() {
//        this.consulClient = new ConsulClient();
//    }
//
//    @Override
//    protected void handleMessageInternal(Message<?> message) {
//        if (this.logger.isTraceEnabled()) {
//            this.logger.trace("Publishing message" + message);
//        }
//        System.out.println("message.toString()");
//        System.out.println(message.toString());
//        // 转换成 String
//        Object payload = message.getPayload();
//        if (payload instanceof byte[]) {
//            payload = new String((byte[]) payload);
//        }
//        Response<Event> event = this.consulClient.eventFire("this.eventName", (String) payload,
//                new EventParams(), QueryParams.DEFAULT);
//    }
//
//    private void run() {
//        Long index = 0L;
//        var queryBuilder = QueryParams.Builder.builder();
//        var requestBuilder = EventListRequest.newBuilder();
//        while (true) {
//            queryBuilder.setIndex(index);
//            requestBuilder.setQueryParams(queryBuilder.build());
//            Response<List<Event>> result = this.consulClient.eventList(requestBuilder.build());
//            index = result.getConsulIndex();
//            if (result == null || result.getValue() == null || result.getValue().size() <= 0) {
//                continue;
//            }
//            for (Event event : result.getValue()) {
//                log.debug(event.toString());
//            }
//        }
//    }
//}
