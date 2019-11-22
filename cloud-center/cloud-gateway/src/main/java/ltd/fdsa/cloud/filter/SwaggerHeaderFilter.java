//package ltd.fdsa.cloud.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//
//import org.springframework.http.server.reactive.ServerHttpRequest;
//
//import org.springframework.stereotype.Component;
//
//import org.springframework.web.server.ServerWebExchange;
//
//import lombok.extern.slf4j.Slf4j;
//import ltd.fdsa.cloud.config.DocumentationConfig;
//
//@Component
//@Slf4j
//public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory<Object> {
//
//	private static final String HEADER_NAME = "X-Forwarded-Prefix";
//
//	private static final String HOST_NAME = "X-Forwarded-Host";
//
//	@Override
//
//	public GatewayFilter apply(Object config) {
//
//		return (exchange, chain) -> {
//
//			ServerHttpRequest request = exchange.getRequest();
//
//			String path = request.getURI().getPath();
//			int index = path.lastIndexOf(DocumentationConfig.API_URI);
//			if (index <= 0) {
//				return chain.filter(exchange);
//			}
//
//			String basePath = path.substring(0, index);
//
//			ServerHttpRequest newRequest = request.mutate().header(HOST_NAME, basePath).header(HEADER_NAME, basePath).build();
//
//			ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
//
//			return chain.filter(newExchange);
//
//		};
//
//	}
//
//}
