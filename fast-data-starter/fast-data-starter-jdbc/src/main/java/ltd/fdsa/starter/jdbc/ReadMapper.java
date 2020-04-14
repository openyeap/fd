package ltd.fdsa.starter.jdbc;

 
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByExampleRowBoundsMapper;
import tk.mybatis.mapper.common.rowbounds.SelectRowBoundsMapper;
 
public interface ReadMapper<T> extends SelectByExampleRowBoundsMapper<T>, SelectRowBoundsMapper<T>,
		SelectByExampleMapper<T>, SelectCountByExampleMapper<T>, BaseSelectMapper<T>, Marker {

}
