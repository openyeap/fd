package ltd.fdsa.audit.action.model;

import lombok.Getter;

/**
 * @date 2018/10/15
 */
@Getter
public class SystemType extends BusinessType {

    /**
     * 日志类型
     */
    protected Byte type;

    public SystemType(String message) {
        super(message);
    }

    public SystemType(String name, String message) {
        super(name, message);
    }
}
