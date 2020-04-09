//package com.daoshu.cloud.gateway.filter;
//
//import com.daoshu.cloud.gateway.config.DocumentationConfig;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//
///**
// * @Classname SwaggerHeaderFilter
// * @Description TODO
// * @Date 2020/3/13 15:35
// * @Author ??
// */
//@Component
//@Slf4j
//public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory<Object> {
////    private static final String HEADER_NAME = "X-Forwarded-Prefix";
//    private static final String HOST_NAME = "X-Forwarded-Host";
//
//    @Override
//    public GatewayFilter apply(Object config) {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            String path = request.getURI().getPath();
//            int index = path.lastIndexOf(DocumentationConfig.API_URI);
//            if (index <= 0) {
//                return chain.filter(exchange);
//            }
//            
//        	log.info("-----------SwaggerHeaderFilter------------");
//            String basePath = path.substring(0, index);
//            ServerHttpRequest newRequest = request.mutate()
//                    .header(HOST_NAME, basePath)
//                    //????basePath??,???~~~  ???springframework????
////                    .header(HEADER_NAME, basePath)
//                    .build();
//            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
//            return chain.filter(newExchange);
//        };
//    }
//}
