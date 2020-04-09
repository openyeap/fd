package ltd.fdsa.cloud.model;

import lombok.Data;

/**
 * @Classname ServiceEmpower
 * @Description TODO
 * @Date 2020/3/18 9:12
 * @Author 高进
 */
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
