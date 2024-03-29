package cn.zhumingwu.cloud.filter;

import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.cloud.constant.Constant;
import cn.zhumingwu.cloud.service.IMockRuleService;
import cn.zhumingwu.cloud.service.impl.MockRuleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class MockRuleFilter implements GlobalFilter, Ordered {

    @Autowired
    private IMockRuleService mockRuleService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String fullPath = request.getPath().pathWithinApplication().value();
        ServerHttpResponse response = exchange.getResponse();
        if (MockRuleServiceImpl.mockRuleMap.containsKey(fullPath) && MockRuleServiceImpl.mockRuleMap.get(fullPath).getStatus() == Constant.MOCK_RULE_STATUS_OPEN) {
            try {
                var result = mockRuleService.getMockData(fullPath).getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(result);
                response.setStatusCode(HttpStatus.OK);
                // 指定编码，否则在浏览器中会中文乱码
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                return response.writeWith(Mono.just(buffer));
            } catch (Exception e) {
                log.error("获取mock数据异常", e);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -101;
    }
}
