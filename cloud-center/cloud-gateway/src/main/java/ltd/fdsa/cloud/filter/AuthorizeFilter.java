//package ltd.fdsa.cloud.filter;
//
//import java.io.UnsupportedEncodingException;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//
//import lombok.extern.slf4j.Slf4j;
//import ltd.fdsa.cloud.Application;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//public class AuthorizeFilter implements GlobalFilter, Ordered {
//
//	private static final String AUTHORIZE_TOKEN = "token";
//	private static final String AUTHORIZE_UID = "uid";
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		log.debug("test");
//		ServerHttpRequest request = exchange.getRequest();
//		HttpHeaders headers = request.getHeaders();
//		String token = headers.getFirst(AUTHORIZE_TOKEN);
//		String uid = headers.getFirst(AUTHORIZE_UID);
//		if (token == null) {
//			token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
//		}
//		if (uid == null) {
//			uid = request.getQueryParams().getFirst(AUTHORIZE_UID);
//		}
//
//		Flux<DataBuffer> body = exchange.getRequest().getBody();
//
//		body.subscribe(buffer -> {
//			byte[] bytes = new byte[buffer.readableByteCount()];
//			buffer.read(bytes);
//			DataBufferUtils.release(buffer);
//			try {
//				String bodyString = new String(bytes, "utf-8");
//				log.info("bodyString");
//				log.info(bodyString);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		});
//
////        ServerHttpResponse response = exchange.getResponse();
////        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
////            response.setStatusCode(HttpStatus.UNAUTHORIZED);
////            return response.setComplete();
////        }
////      TODO:
////        String authToken = stringRedisTemplate.opsForValue().get(uid);
////        if (authToken == null || !authToken.equals(token)) {
////            response.setStatusCode(HttpStatus.UNAUTHORIZED);
////            return response.setComplete();
////        }
//
//		return chain.filter(exchange);
//	}
//
//	@Override
//	public int getOrder() {
//		return -1000;
//	}
//
//}

package ltd.fdsa.cloud.filter;

import lombok.extern.log4j.Log4j2;
import ltd.fdsa.cloud.util.ConsulServerUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String prefix = "auth";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().pathWithinApplication().value();
        String authKey = prefix + path;
        log.info(authKey);
        String val = ConsulServerUtil.getInstance().getALLConsulKV().get(authKey);
        if (val == null) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        } else if (!"".equals(val)) {
            //TODO 校验token，暂时先不做，直接从header里面拿一个字符串，暂时不做校验
            String roles = request.getHeaders().getFirst("token");
            if(roles == null || "".equals(roles)) {
                response.setStatusCode(HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
                return response.setComplete();
            }
            String[] roleArr = roles.split(",");
            boolean flag = false;
            String[] urlNeedRoles = val.split(",");
            for(String role : roleArr) {
                if("".equals(role)) {
                    continue;
                }
                for(String urlRole : urlNeedRoles) {
                    if(role.equals(urlRole)) {
                        flag = true;
                        break;
                    }
                }
                if(flag) {
                    break;
                }
            }
            if(!flag) {
                response.setStatusCode(HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}