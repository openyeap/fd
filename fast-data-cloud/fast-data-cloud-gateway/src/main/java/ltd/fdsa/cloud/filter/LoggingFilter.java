package ltd.fdsa.cloud.filter;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.util.RequestUtil;
import ltd.fdsa.core.util.NamingUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ConditionalOnProperty(prefix = "filter", name = "loggingFilter", havingValue = "true")
@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    public LoggingFilter() {
        NamingUtils.formatLog(log,"Loaded GlobalFilter Logging");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest serverHttpRequest = exchange.getRequest();

        String param = "";

        if ("GET".equals(serverHttpRequest.getMethodValue())) {
            param = serverHttpRequest.getQueryParams().toString();

        } else {
            param = RequestUtil.resolveBodyFromRequest(serverHttpRequest);

        }

        String info = String.format("Method:{%s} Host:{%s} Path:{%s} Query:%s",
                serverHttpRequest.getMethod().name(),
                serverHttpRequest.getURI().getHost(),
                serverHttpRequest.getURI().getPath(),
                param
        );

        log.info(info);

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange.mutate().request(serverHttpRequest).build()).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                log.info(serverHttpRequest.getURI().getRawPath() + " : " + executeTime + "ms");
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}