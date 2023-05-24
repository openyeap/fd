package cn.zhumingwu.cloud.filter;

import cn.zhumingwu.cloud.jwt.IJwtToken;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.cloud.constant.Constant;
import cn.zhumingwu.cloud.jwt.constant.JwtConstant;
import cn.zhumingwu.cloud.jwt.model.JwtValidationResult;
import cn.zhumingwu.cloud.jwt.model.JwtValidationResultType;
import cn.zhumingwu.cloud.service.AuthorizeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AuthorizeFilter extends BaseFilter {

    @Autowired
    private AuthorizeService consulService;

    @Autowired
    private IJwtToken jwt;

    private HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    @Override
    public boolean access(ServerHttpRequest request) {
        String path = "";
        switch (request.getMethod()) {
            case GET:
                path = "GET " + fullPath;
                break;
            case POST:
                path = "POST " + fullPath;
                break;
            case PUT:
                path = "PUT " + fullPath;
                break;
            case DELETE:
                path = "DELETE " + fullPath;
                break;
            default:
                break;
        }
        List<String> id = request.getHeaders().get(Constant.HEAD_USER_ID);
        if (id != null) {
            httpStatus = HttpStatus.BAD_REQUEST;
            return false;
        }
        List<String> roleList = request.getHeaders().get(Constant.HEAD_USER_ROLES);
        if (roleList != null) {
            httpStatus = HttpStatus.BAD_REQUEST;
            return false;
        }
        List<String> tokenList = request.getHeaders().get(JwtConstant.HEADER_STRING);
        String token = "";
        String[] userRoles = null;
        String userId = "";
        if (tokenList != null && tokenList.size() > 0) {
            token = tokenList.get(0);
            if (token.startsWith(JwtConstant.TOKEN_PREFIX)) {
                token = token.substring(JwtConstant.TOKEN_PREFIX.length() + 1);
                JwtValidationResult jvr = jwt.validate(token);
                if (jvr.getResultType() == JwtValidationResultType.TOKEN_VALID) {
                    userRoles = jvr.getUser().getRoles().split(",");
                    userId = jvr.getUser().getId();
                    String roles = "";
                    if (userRoles != null) {
                        roles = StringUtils.join(userRoles, ";");
                    }
                    ServerHttpRequest.Builder mutate = request.mutate();
                    mutate.header(Constant.HEAD_USER_ID, userId);
                    mutate.header(Constant.HEAD_USER_ROLES, roles);
                    newRequest = mutate.build();
                }
            }
        }
        return consulService.checkAuthorize(path, userRoles);
    }

    @Override
    protected ResponseEntity<Object> result() {
        return ResponseEntity.status(this.httpStatus).build();
    }

    @Override
    public int getOrder() {
        return -99;
    }
}
