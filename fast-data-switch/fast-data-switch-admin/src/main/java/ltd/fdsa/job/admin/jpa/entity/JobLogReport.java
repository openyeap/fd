package ltd.fdsa.job.admin.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ltd.fdsa.database.entity.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "job_report")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobLogReport extends BaseEntity<Integer> {
    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Date triggerDay;

    private int runningCount;
    private int sucCount;
    private int failCount;

}
