package ltd.fdsa.kafka.stream.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ltd.fdsa.database.entity.BaseEntity;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "etl_task")
@Entity
public class EtlTask extends BaseEntity<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @Id
    @Column(name = "task_id")
    private Integer id;



    /**
     * 任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 接入源 source连接器名称
     */
    @Column(name = "source_connector_name")
    private String sourceConnectorName;

    /**
     * 接入源 source连接器配置
     */
    @Column(name = "source_connector_config")
    private String sourceConnectorConfig;

    /**
     * 贴源 sink连接器名称
     */
    @Column(name = "sink_connector_name")
    private String sinkConnectorName;

    /**
     * 贴源 sink连接器配置
     */
    @Column(name = "sink_connector_config")
    private String sinkConnectorConfig;


    /**
     * 落地 source连接器名称
     */
    @Column(name = "target_source_connector_name")
    private String targetSourceConnectorName;

    /**
     * 落地 source连接器配置
     */
    @Column(name = "target_source_connector_config")
    private String targetSourceConnectorConfig;

    /**
     * 落地 sink连接器名称
     */
    @Column(name = "target_sink_connector_name")
    private String targetSinkConnectorName;

    /**
     * 落地 sink连接器配置
     */
    @Column(name = "target_sink_connector_config")
    private String targetSinkConnectorConfig;

    /**
     * 是否数据处理（true / false）
     */
    @Column(name = "is_data_handling")
    private Boolean isDataHandling;
}
