package ltd.fdsa.client.mybatis.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ltd.fdsa.database.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Table(name = "t_user")
public class User extends BaseEntity<Integer> {

    private static final long serialVersionUID = -7155610581355123677L;

    @Id
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "end_time")
    private LocalDate endTime;

    @Column(name = "from_time")
    private LocalDate fromTime;

    @Column(name = "is_locked")
    private String isLocked;

    @Column(name = "lock_time")
    private LocalDate lockTime;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "multi_login")
    private String multiLogin;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "user_name")
    private String userName;
}
