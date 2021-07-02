package ltd.fdsa.client.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ltd.fdsa.database.entity.BaseEntity;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhumingwu
 * @since 2020-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_role")
public class Role extends BaseEntity<Integer> {

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "value" )
    private  Integer value;

}
