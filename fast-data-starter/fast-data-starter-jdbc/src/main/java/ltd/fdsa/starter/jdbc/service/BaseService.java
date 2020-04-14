package ltd.fdsa.starter.jdbc.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import ltd.fdsa.starter.jdbc.ReadMapper;
import ltd.fdsa.starter.jdbc.WriteMapper;
import tk.mybatis.mapper.entity.Example;

public class BaseService<W extends WriteMapper<T>, R extends ReadMapper<T>, T> implements IService<T> {

	@Autowired
	protected W writeMapper;

	@Autowired
	protected R readMapper;

	@Override
	public int delete(T record) {
		return writeMapper.delete(record);
	}

	@Override
	public int insert(T record) {
		return this.writeMapper.insert(record);
	}

	@Override
	public T selectOne(int id) {
		return this.readMapper.selectByPrimaryKey(id);
	}

	@Override
	public T selectOne(T record) {
		return this.readMapper.selectOne(record);
	}

	@Override
	public int deleteOne(int id) {
		return this.writeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<T> selectByExample(Example example) {
		return this.readMapper.selectByExample(example);
	}

	@Override
	public List<T> selectByExampleAndRowBounds(Example example, int offset, int limit) {
		RowBounds rowBounds = new RowBounds(offset, limit);
		return this.readMapper.selectByExampleAndRowBounds(example, rowBounds);
	}

}
