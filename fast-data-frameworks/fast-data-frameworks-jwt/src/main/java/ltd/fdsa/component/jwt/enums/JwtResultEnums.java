package ltd.fdsa.component.jwt.enums;

import lombok.Getter;
import ltd.fdsa.web.enums.ResultCode;

/**
 * jwt结果集枚举
 *
 */
@Getter
public enum JwtResultEnums implements ResultCode {

    /**
     * token问题
     */
    TOKEN_ERROR(301, "token无效"),
    TOKEN_EXPIRED(302, "token已过期"),

    /**
     * 账号问题
     */
    AUTH_REQUEST_ERROR(401, "用户名或密码错误"),
    AUTH_REQUEST_LOCKED(402, "该账号已被冻结"),
    ;

    private int code;

    private String message;

    JwtResultEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
