package com.daoshu.datasource.database;

import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByExampleRowBoundsMapper;
import tk.mybatis.mapper.common.rowbounds.SelectRowBoundsMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author system
 * @since 2019-01-29
 */

public interface ReadMapper<T> extends SelectByExampleRowBoundsMapper<T>, SelectRowBoundsMapper<T>,
		SelectByExampleMapper<T>, SelectCountByExampleMapper<T>, BaseSelectMapper<T>, Marker {

}
