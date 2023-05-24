package cn.zhumingwu.database.mybatis.plus.mapper.writer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.zhumingwu.database.entity.BaseEntity;

public interface WriteMapper<Entity extends BaseEntity>
        extends BaseMapper<Entity> {
}
