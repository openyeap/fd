package ltd.fdsa.starter.jdbc.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;

public interface IService<T> {
	int delete(T record);

	T selectOne(int id);

	int insert(T record);

	T selectOne(T record);

	int deleteOne(int id);

	List<T> selectByExample(Example example);

	List<T> selectByExampleAndRowBounds(Example example, int offset, int limit);

}
