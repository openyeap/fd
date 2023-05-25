package cn.zhumingwu.dataswitch.admin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "job_log")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobLog extends BaseEntity<Integer> {
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "job_id")
    private Integer jobId;// 任务主键ID
    @Column(name = "task_id")
    private Integer taskId; //  任务主键ID

    private int fromLineNum;
    private int toLineNum;
    private String content;
}
