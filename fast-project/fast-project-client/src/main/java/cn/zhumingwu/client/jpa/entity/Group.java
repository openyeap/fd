package cn.zhumingwu.client.jpa.entity;

import cn.zhumingwu.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
