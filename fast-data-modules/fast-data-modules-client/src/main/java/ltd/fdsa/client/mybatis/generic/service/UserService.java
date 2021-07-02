package ltd.fdsa.client.mybatis.generic.service;

//import lombok.extern.slf4j.Slf4j;

import ltd.fdsa.client.mybatis.entity.User;
import ltd.fdsa.database.service.DataAccessService;

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
