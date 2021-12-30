package ltd.fdsa.database.entity;

import lombok.Data;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity<ID> implements IEntity<ID>, ISupportAudit<ID>, ISupportLogicDelete, Serializable {

    /**
     * 名称
     */
    @Column(name = "name")
    @Order(0)
    private String name;

    /**
     * 类型
     */
    @Column(name = "type")
    @Order(0)
    private Byte type = 0;


    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @CreatedBy
    @Column(name = "create_by")
    private ID createBy;

    /**
     * 更新者
     */
    @LastModifiedBy
    @Column(name = "update_by")
    private ID updateBy;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort = 0;

    /**
     * 备注
     */
    @Column(name = "remark")
    @Order(1000)
    private String remark;

    /**
     * 状态
     */
    @Column(name = "status")
    @Order(1000)
    private Status status = Status.OK;

}