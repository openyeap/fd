package cn.zhumingwu.client.mybatis.generic.mapper.reader;

import cn.zhumingwu.client.mybatis.entity.User;
import cn.zhumingwu.database.mybatis.generic.annotation.MybatisGenericMapper;
import cn.zhumingwu.database.mybatis.generic.mapper.reader.ReadMapper;

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
