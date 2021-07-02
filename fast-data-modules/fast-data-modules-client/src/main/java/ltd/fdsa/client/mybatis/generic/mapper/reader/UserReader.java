package ltd.fdsa.client.mybatis.generic.mapper.reader;

import ltd.fdsa.client.mybatis.entity.User;
import ltd.fdsa.database.mybatis.generic.annotation.MybatisGenericMapper;
import ltd.fdsa.database.mybatis.generic.mapper.reader.ReadMapper;

/**
 * <p>
 * 数据访问类
 * </p>
 *
 * @author zhumingwu
 * @since 2020-12-09
 */
@MybatisGenericMapper
public interface UserReader extends ReadMapper<User> {

}
