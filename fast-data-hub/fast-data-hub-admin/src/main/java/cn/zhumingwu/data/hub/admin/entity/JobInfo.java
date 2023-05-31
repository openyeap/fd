package cn.zhumingwu.data.hub.admin.entity;

import cn.zhumingwu.data.hub.admin.enums.ExecutorRouteStrategy;
import cn.zhumingwu.data.hub.admin.enums.ScheduleType;
import cn.zhumingwu.dataswitch.core.job.enums.ExecutorBlockStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_job")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobInfo extends BaseEntity<Long> {
    @Id
    @Column(name = "job_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            // 主键ID
    private String author;      // 负责人
    private String alarmEmail;  // 报警邮件

    private String executorHandler;                      // 执行器，任务Handler名称
    private String executorParam;                        // 执行器，任务参数,多个逗号分隔
    private String executorExpression;                   // 执行器，选择表达式,多个逗号分隔
    private ExecutorRouteStrategy executorRouteStrategy; // 执行器路由策略

    private ScheduleType scheduleType;                   // 自动触发（无，Cron、固定间隔触发、固定延时触发）
    private String scheduleExpression;                   // 触发表达式


    private ExecutorBlockStrategy executorBlockStrategy; // 阻塞处理策略

    private int executorTimeout;    // 任务执行超时时间，单位秒
    private int executorRetryTimes; // 失败重试次数
    private String childJobId;      // 子任务ID，多个逗号分隔

    private int triggerStatus;      // 调度状态：0-停止，1-运行
    private long triggerLastTime;   // 上次调度时间
    private long triggerNextTime;   // 下次调度时间
//    private long triggerNextTime1; // 下次调度时间
//    private long triggerNextTime2; // 下次调度时间
//    private long triggerNextTime3; // 下次调度时间
//    private long triggerNextTime4; // 下次调度时间


}
