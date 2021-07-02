package ltd.fdsa.cloud.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;

@Component
@Slf4j
public class GlobalLogFilter implements GlobalFilter, Ordered {


    private static final String START_TIME = "startTime";
    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        // 请求路径
        String path = request.getPath().pathWithinApplication().value();
        // 请求schema: http/https
        String scheme = request.getURI().getScheme();
        // 请求方法
        HttpMethod method = request.getMethod();
        // 路由服务地址
        URI targetUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        // 请求头
        HttpHeaders headers = request.getHeaders();
        // 设置startTime
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        // 获取请求地址
        InetSocketAddress remoteAddress = request.getRemoteAddress();


        MultiValueMap<String, String> formData = null;
        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setPath(path);
        accessRecord.setSchema(scheme);
        accessRecord.setMethod(method.name());
        accessRecord.setTargetUri(targetUri.toString());
        accessRecord.setRemoteAddress(remoteAddress.toString());
        accessRecord.setHeaders(headers);

        if (method == HttpMethod.GET) {
            formData = request.getQueryParams();
            accessRecord.setFormData(formData);
        }

        if (method == HttpMethod.POST) {
            Mono<Void> voidMono = null;
            if (headers.getContentType().equals(MediaType.APPLICATION_JSON) || headers.getContentType().equals(MediaType.APPLICATION_FORM_URLENCODED)) {
                // JSON
                voidMono = readBody(exchange, chain, accessRecord);

            }


            if (voidMono != null) {
                return voidMono.then(Mono.fromRunnable(() -> {
                    writeAccessRecord(accessRecord);
                }));
            }

        }

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            writeAccessRecord(accessRecord);
        }));
    }

    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, AccessRecord accessRecord) {

        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {

            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);

            Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                DataBufferUtils.retain(buffer);
                return Mono.just(buffer);
            });


            // 重写请求体,因为请求体数据只能被消费一次
            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedFlux;
                }
            };

            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

            return ServerRequest.create(mutatedExchange, messageReaders)
                    .bodyToMono(String.class)
                    .doOnNext(objectValue -> {
                        accessRecord.setBody(objectValue);
                        writeAccessRecord(accessRecord);
                    }).then(chain.filter(mutatedExchange));
        });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private void writeAccessRecord(AccessRecord accessRecord) {

        log.info("\n\n start------------------------------------------------- \n " +
                        "请求路径:{}\n " +
                        "scheme:{}\n " +
                        "请求方法:{}\n " +
                        "目标服务:{}\n " +
                        "请求头:{}\n " +
                        "远程IP地址:{}\n " +
                        "表单参数:{}\n " +
                        "请求体:{}\n " +
                        "end------------------------------------------------- \n ",
                accessRecord.getPath(), accessRecord.getSchema(), accessRecord.getMethod(), accessRecord.getTargetUri(), accessRecord.getHeaders(), accessRecord.getRemoteAddress(), accessRecord.getFormData(), accessRecord.getBody());
    }
}
