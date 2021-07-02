package ltd.fdsa.job.admin.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ltd.fdsa.database.entity.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "job_info")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobInfo extends BaseEntity<Integer> {
    @Id
    @Column(name = "job_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id; // 主键ID

    private Integer groupId; // 执行器主键ID
    private String cronExpression; // 任务执行CRON表达式

    private String author; // 负责人
    private String alarmEmail; // 报警邮件

    private String executorRouteStrategy; // 执行器路由策略
    private String executorHandler; // 执行器，任务Handler名称
    private String executorParam; // 执行器，任务参数
    private String executorBlockStrategy; // 阻塞处理策略
    private int executorTimeout; // 任务执行超时时间，单位秒
    private int executorFailRetryCount; // 失败重试次数

    private String childJobId; // 子任务ID，多个逗号分隔

    private int triggerStatus; // 调度状态：0-停止，1-运行
    private long triggerLastTime; // 上次调度时间
    private long triggerNextTime; // 下次调度时间


}
