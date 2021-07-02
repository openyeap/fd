package ltd.fdsa.client.mybatis.generic.mapper.writer;

import ltd.fdsa.client.mybatis.entity.User;
import ltd.fdsa.database.mybatis.generic.annotation.MybatisGenericMapper;
import ltd.fdsa.database.mybatis.generic.mapper.writer.WriteMapper;

/**
 * <p>
 * 数据访问类
 * </p>
 *
 * @author zhumingwu
 * @since 2020-12-09
 */
@MybatisGenericMapper
public interface UserWriter extends WriteMapper<User> {

}
