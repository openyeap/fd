package com.daoshu.datasource.database;

import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.BaseDeleteMapper;
import tk.mybatis.mapper.common.base.BaseInsertMapper;
import tk.mybatis.mapper.common.base.BaseUpdateMapper;
import tk.mybatis.mapper.common.example.DeleteByExampleMapper;
import tk.mybatis.mapper.common.example.UpdateByExampleMapper;
import tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author system
 * @since 2019-01-29
 */
public interface WriteMapper<T> extends BaseDeleteMapper<T>, BaseInsertMapper<T>, BaseUpdateMapper<T>,
		DeleteByExampleMapper<T>, UpdateByExampleMapper<T>, UpdateByExampleSelectiveMapper<T>, Marker {

}
