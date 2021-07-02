package ltd.fdsa.audit.action.model;

import lombok.Getter;

/**
 * @date 2018/10/15
 */
@Getter
public class ActionModel {
    /**
     * 日志名称
     */
    protected String name;

    /**
     * 日志类型
     */
    protected Byte type;
}
