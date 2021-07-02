package ltd.fdsa.cloud.config;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.util.NamingUtils;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(10000); //单位为ms
        factory.setConnectTimeout(10000); //单位为ms
        return factory;
    }

    @Bean
    @Order(-1)
    public GlobalFilter a() {
        return (exchange, chain) -> {
            NamingUtils.formatLog(log,"first pre filter");
            return ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders())
                    .bodyToMono(String.class)
                    .doOnNext(bodyData -> {
                        NamingUtils.formatLog(log, bodyData);
                    }).then(chain.filter(exchange)).then(Mono.fromRunnable(() -> {
                        NamingUtils.formatLog(log,"third post filter");
                    }));
        };
    }

    @Bean
    @Order(0)
    public GlobalFilter b() {
        return (exchange, chain) -> {
            NamingUtils.formatLog(log,"second pre filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                NamingUtils.formatLog(log,"second post filter");
            }));
        };
    }

    @Bean
    @Order(1)
    public GlobalFilter c() {
        return (exchange, chain) -> {
            NamingUtils.formatLog(log,"third pre filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                NamingUtils.formatLog(log,"first post filter");
            }));
        };
    }
}