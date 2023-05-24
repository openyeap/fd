package cn.zhumingwu.client.mybatis.generic.service;

//import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.client.mybatis.entity.User;
import cn.zhumingwu.database.service.DataAccessService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhumingwu
 * @since 2020-12-09
 */
//@Slf4j
public interface UserService extends DataAccessService<User, Integer> {

}
