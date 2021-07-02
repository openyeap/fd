package ltd.fdsa.cloud.filter;

import io.netty.buffer.ByteBufAllocator;
import lombok.var;
import ltd.fdsa.cloud.property.AuthProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class CustomApiFilter implements GlobalFilter, Ordered {


    private static final Log log = LogFactory.getLog(GatewayFilter.class);

    @Autowired
    private AuthProperties mySQLProperties;
    private ServerHttpRequest request;
    private ServerHttpResponse response;
//    @Autowired
//    private IMockRuleService mockRuleService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        this.request = exchange.getRequest();
        this.response = exchange.getResponse();
        this.request.getPath().pathWithinApplication().toString();
//
//        AtomicReference<byte[]> atomicReference = new AtomicReference<>();
//        Flux<DataBuffer> body = this.request.getBody();
//        body.subscribe(dataBuffer -> {
//            byte[] bytes = new byte[dataBuffer.readableByteCount()];
//            dataBuffer.read(bytes);
//            DataBufferUtils.release(dataBuffer);
//            atomicReference.set(bytes);
//            log.info("do get Body: {}");
//            log.info(new String(bytes));
//        });
//        result = chain.filter(exchange.mutate().request(new ServerHttpRequestDecorator(this.request) {
//            @Override
//            public Flux<DataBuffer> getBody() {
//                if (atomicReference.get() != null) {
//                    NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
//                    DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(atomicReference.get().length);
//                    buffer.write(atomicReference.get());
//                    return Flux.just(buffer);
//                }
//                return this.getBody();
//            }
//        }).build());

        Mono<Void> result = ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, (serverHttpRequest) -> {

            serverHttpRequest.getBody().subscribe(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                log.info("do get Body: {}");
                log.info(new String(bytes));
            });
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        });
        return result
                .doOnNext(va -> {
                    log.info("do On Next: {}" + this.request.getPath());
                })
                .doOnSubscribe(subscription -> {
                    log.info("do On Subscribe: {}" + this.request.getPath());
                })
                .doFinally(m -> {
                    log.info("do Finally: {}" + this.request.getPath());
                })
                .doOnError(ex -> {
                    log.error("do On Error");
                });
    }


    @Override
    public int getOrder() {
        return -1;
    }
}
