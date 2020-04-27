package ltd.fdsa.test.model.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.baomidou.mybatisplus.annotation.TableId;
//import java.time.LocalDateTime;
//import com.baomidou.mybatisplus.annotation.TableField;
//import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "r_group_role")
public class GroupRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "group_role_id")
    private Integer groupRoleId;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_by")
    private Integer updateBy;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_display")
    private Boolean isDisplay;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "remark")
    private String remark;


}
