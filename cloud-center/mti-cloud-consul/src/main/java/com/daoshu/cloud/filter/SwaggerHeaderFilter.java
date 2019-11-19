package com.daoshu.cloud.filter;
 

import org.springframework.cloud.gateway.filter.GatewayFilter;

import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import com.daoshu.cloud.configure.DocumentationConfig; 
 
@Component

public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory<Object> {

    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    private static final String HOST_NAME = "X-Forwarded-Host";

    @Override

    public GatewayFilter apply(Object config) {

        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            String path = request.getURI().getPath();

            if (!(path.contains( DocumentationConfig.API_URI))) {

                return chain.filter(exchange);

            }

            String basePath = path.substring(0, path.lastIndexOf(DocumentationConfig.API_URI));

            ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).build();

            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

            return chain.filter(newExchange);

        };

    }

}
