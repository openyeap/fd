package cn.zhumingwu.audit.action.model;

import lombok.Getter;

/**
 
 */
@Getter
public class LoginType extends BusinessType {

    /**
     * 日志类型
     */
    protected Byte type;

    public LoginType(String message) {
        super(message);
    }

    public LoginType(String name, String message) {
        super(name, message);
    }
}
