package cn.zhumingwu.database.mybatis.generic.mapper.writer;

import cn.zhumingwu.database.entity.BaseEntity;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.BaseDeleteMapper;
import tk.mybatis.mapper.common.base.BaseInsertMapper;
import tk.mybatis.mapper.common.base.BaseUpdateMapper;
import tk.mybatis.mapper.common.example.DeleteByExampleMapper;
import tk.mybatis.mapper.common.example.UpdateByExampleMapper;
import tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper;

public interface WriteMapper<Entity extends BaseEntity>
        extends BaseDeleteMapper<Entity>, BaseInsertMapper<Entity>, BaseUpdateMapper<Entity>, DeleteByExampleMapper<Entity>, UpdateByExampleMapper<Entity>, UpdateByExampleSelectiveMapper<Entity>, Marker {
}
