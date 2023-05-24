package cn.zhumingwu.client.mybatis.generic.mapper.writer;

import cn.zhumingwu.client.mybatis.entity.User;
import cn.zhumingwu.database.mybatis.generic.annotation.MybatisGenericMapper;
import cn.zhumingwu.database.mybatis.generic.mapper.writer.WriteMapper;

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
