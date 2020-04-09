package ltd.fdsa.cloud.filter;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.Application; 
import ltd.fdsa.common.model.view.ResponseResult;
import ltd.fdsa.common.util.LicenseUtils;
import java.util.List;
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

@Slf4j 
@Component
public class AuthorizeFilter extends BaseFilter {

 

    @Override
    public boolean access(ServerHttpRequest request) {
 
//        String path = "";
//        switch (request.getMethod()) {
//            case GET:
//                path = "GET " + fullPath;
//                break;
//            case POST:
//                path = "POST " + fullPath;
//                break;
//            case PUT:
//                path = "PUT " + fullPath;
//                break;
//            case DELETE:
//                path = "DELETE " + fullPath;
//            default:
//                break;
//        }
//        List<String> tokenList = request.getHeaders().get(JwtConstant.HEADER_STRING);
//        String token = "";
//        if(tokenList != null && tokenList.size() > 0) {
//            token = tokenList.get(0);
//            if(!token.startsWith(JwtConstant.TOKEN_PREFIX)) {
//                token = "";
//            } else {
//                token = token.substring(JwtConstant.TOKEN_PREFIX.length() + 1);
//            }
//        }
        return true;
    }

    @Override
    protected HttpStatus errorStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    protected ResponseResult<String> errorBody() {
        return ResponseResult.fail(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), "");
    }

    @Override
    public int getOrder() {
        return -99;
    }
}
