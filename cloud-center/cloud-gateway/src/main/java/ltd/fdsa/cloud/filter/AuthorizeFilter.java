package ltd.fdsa.cloud.filter;

import lombok.extern.log4j.Log4j2;
import ltd.fdsa.cloud.service.ConsulService;
import ltd.fdsa.common.util.LicenseUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ConsulService certConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().pathWithinApplication().value();
        if(StringUtils.isEmpty(path)) {
            return chain.filter(exchange);
        }
        //证书校验
        String[] paths = path.split( "/");
        if(paths.length>=2)
        {
        	String serverName = paths[1];
           if(!certConfig.checkServiceAuth(serverName)) {
        	   response.setStatusCode(HttpStatus.PAYMENT_REQUIRED);
               log.info(LicenseUtils.getMachineCode(serverName));
               return response.setComplete();
           }
        }
        //权限校验
        String authKey = prefix + path;
        String val = certConfig.getAuthConfig().get(authKey);
        if (val == null) {
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
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