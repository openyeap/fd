package ltd.fdsa.client.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ltd.fdsa.database.entity.BaseEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_group")
@Entity
public class Group extends BaseEntity<Integer> {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
}
