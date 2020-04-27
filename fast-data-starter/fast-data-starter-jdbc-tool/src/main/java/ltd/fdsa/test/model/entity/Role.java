package ltd.fdsa.test.model.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.baomidou.mybatisplus.annotation.TableId;
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
@Table(name = "s_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "name")
    private String name;


}
