package ltd.fdsa.server.model;

import lombok.Data;

@Data
public class ServiceAuthorize {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 机器码
     */
    private String machineCode;
    /**
     * 授权状态
     * true：已授权
     * false：未授权
     */
    private boolean isAuthorize;
    /**
     * 过期时间
     */
    private String expiredDate;
}
