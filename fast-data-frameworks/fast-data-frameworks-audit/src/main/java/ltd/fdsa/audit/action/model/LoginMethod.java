package ltd.fdsa.audit.action.model;

import lombok.Getter;

/**
 * @date 2018/10/15
 */
@Getter
public class LoginMethod extends BusinessMethod {

    /**
     * 日志类型
     */
    protected Byte type;

    public LoginMethod(String method) {
        super(method);
    }

    public LoginMethod(String name, String method) {
        super(name, method);
    }
}
