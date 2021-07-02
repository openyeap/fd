package ltd.fdsa.audit.action.model;

import lombok.Getter;

/**
 * @date 2018/10/15
 */
@Getter
public class SystemMethod extends BusinessMethod {

    /**
     * 日志类型
     */
    protected Byte type;

    public SystemMethod(String method) {
        super(method);
    }

    public SystemMethod(String name, String method) {
        super(name, method);
    }
}
