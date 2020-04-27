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
@Table(name = "s_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "from_time")
    private LocalDateTime fromTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "type")
    private Integer type;

    @Column(name = "mutil_login")
    private Boolean mutilLogin;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "lock_time")
    private LocalDateTime lockTime;

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

    @Column(name = "salt")
    private String salt;


}
