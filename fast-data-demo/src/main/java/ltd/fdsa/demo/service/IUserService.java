package ltd.fdsa.demo.service;

import java.util.List;

import ltd.fdsa.demo.model.entity.User;
import ltd.fdsa.demo.model.view.ViewUser;
import ltd.fdsa.starter.jdbc.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author system
 * @since 2019-02-23
 */
public interface IUserService extends IService<User> {
   
	List<User> selectUserByGroupId(String groupId); 
  
}
