package ltd.fdsa.job.admin.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ltd.fdsa.database.entity.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "job_group")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobGroup extends BaseEntity<Integer> {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
 /*
 * 标题
 * */
    private String title;

 //type 执行器定位：0=自动注册、1=手动录入

    private String addressList; // 执行器地址列表，多地址逗号分隔(手动录入)

}
