package ltd.fdsa.database.mybatis.generic.mapper.reader;

import ltd.fdsa.database.entity.BaseEntity;
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
 * @author zhumingwu
 * @since 2019-01-29
 */
public interface ReadMapper<Entity extends BaseEntity>
        extends SelectByExampleRowBoundsMapper<Entity>, SelectRowBoundsMapper<Entity>, SelectByExampleMapper<Entity>, SelectCountByExampleMapper<Entity>, BaseSelectMapper<Entity>, Marker {

}
