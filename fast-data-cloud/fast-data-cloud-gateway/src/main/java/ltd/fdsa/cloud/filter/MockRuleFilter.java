package ltd.fdsa.cloud.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.constant.Constant;
import ltd.fdsa.cloud.service.IMockRuleService;
import ltd.fdsa.cloud.service.impl.MockRuleServiceImpl;
import ltd.fdsa.web.view.Result;
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
                String result = mockRuleService.getMockData(fullPath);
                Object obj = null;
                try {
                    obj = JSONObject.parseObject(result);
                } catch (Exception e) {
                    obj = JSON.parseArray(result);
                }
                byte[] bits = JSONObject.toJSONString(Result.success(obj)).getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bits);
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
