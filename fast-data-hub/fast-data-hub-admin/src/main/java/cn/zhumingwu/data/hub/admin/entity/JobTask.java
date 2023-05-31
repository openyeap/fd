package cn.zhumingwu.data.hub.admin.entity;

import cn.zhumingwu.data.hub.admin.enums.TriggerType;
import cn.zhumingwu.dataswitch.core.job.enums.JobStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "job_task")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobTask extends BaseEntity<Long> {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "job_id")
    private Long jobId; // 任务主键ID

    // executor info
    private String executorInstanceId;
    private String executorHandler;
    private String executorParam;
    private String executorRouterExpression;
    private int executorFailRetryCount;

    // trigger info
    private Date triggerTime;
    private TriggerType triggerType;
    private String triggerMsg;

    // handle info
    private Date handleTime;
    private int handleCode;
    private String handleMsg;
    private JobStatus handleStatus;

    // alarm info
    private int alarmStatus;

}
