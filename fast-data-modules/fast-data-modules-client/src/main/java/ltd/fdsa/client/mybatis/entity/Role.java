package ltd.fdsa.client.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ltd.fdsa.database.entity.BaseEntity;

/**
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

    @TableField(value = "value")
    private Integer value;

}
