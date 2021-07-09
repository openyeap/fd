package ltd.fdsa.kafka.stream.view;

import lombok.Data;

/**
 * @author liuwg
 * @date 2021-04-06
 * 数据处理输入
 */
@Data
public class EtlReq {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;


    /**
     * source 连接器名称
     */
    private String sourceConnectorName;

    /**
     * soruce 配置
     */
    private String sourceConnectorConfig;

    /**
     * sink 连接器名称
     */
    private String sinkConnectorName;

    /**
     * sink 配置
     */
    private String sinkConnectorConfig;

    /**
     * 是否-数据处理
     */
    private boolean isDataHandling;
}
