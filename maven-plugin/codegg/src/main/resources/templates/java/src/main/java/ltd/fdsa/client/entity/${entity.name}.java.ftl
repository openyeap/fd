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
@TableName("${entity.name}")
public class ${entity.name} extends BaseEntity<Integer> {

    <#list entity.fields  as field>
    @TableId(value = "${field.name}", type = IdType.AUTO)
    private ${field.type} ${field.name};
    </#list>
}
