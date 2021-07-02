package ltd.fdsa.job.admin.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ltd.fdsa.database.entity.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "job_user")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class JobUser extends BaseEntity<Integer> {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username; // 账号
    private String password; // 密码
    private int role; // 角色：0-普通用户、1-管理员
    private String permission; // 权限：执行器ID列表，多个逗号分割

    // plugin
    public boolean validPermission(int jobGroup) {
        if (this.role == 1) {
            return true;
        } else {
            if (StringUtils.hasText(this.permission)) {
                for (String permissionItem : this.permission.split(",")) {
                    if (String.valueOf(jobGroup).equals(permissionItem)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
