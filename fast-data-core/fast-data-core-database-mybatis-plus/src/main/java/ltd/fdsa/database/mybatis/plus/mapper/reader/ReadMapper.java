package ltd.fdsa.database.mybatis.plus.mapper.reader;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ltd.fdsa.database.entity.BaseEntity;

/**
 * @Title: MyBatis-plus基础继承类
 */
public interface ReadMapper<Entity extends BaseEntity>
        extends BaseMapper<Entity> {

}
