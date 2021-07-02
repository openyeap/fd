package ltd.fdsa.job.admin.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ltd.fdsa.database.entity.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "job_task")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobTask extends BaseEntity<Integer> {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "group_id")
    private Integer groupId; // 任务组主键ID

    @Column(name = "job_id")
    private Integer jobId; // 任务主键ID

    private String glueType;
    private String glueSource;
    private String glueRemark;

}
