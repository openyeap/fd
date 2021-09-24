package ltd.fdsa.client.entity;


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
@TableName("UserExt")
public class UserExt extends BaseEntity<Integer> {

    @TableId(value = "age", type = IdType.AUTO)
    private Integer age;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableId(value = "name", type = IdType.AUTO)
    private String name;
}
