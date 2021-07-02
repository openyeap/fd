package ltd.fdsa.job.admin.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ltd.fdsa.database.entity.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "job_registry")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobRegistry extends BaseEntity<Integer> {

    @Id
    @Column(name = "registry_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String registryGroup;
    private String registryKey;
    private String registryValue;


}
