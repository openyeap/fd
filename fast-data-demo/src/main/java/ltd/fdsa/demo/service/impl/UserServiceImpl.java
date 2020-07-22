package ltd.fdsa.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.demo.database.readMappers.UserReader;
import ltd.fdsa.demo.database.writeMappers.UserWriter;
import ltd.fdsa.demo.model.entity.User;
import ltd.fdsa.demo.service.IUserService;
import ltd.fdsa.starter.jdbc.ReadMapper;
import ltd.fdsa.starter.jdbc.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务实现类
 *
 * @author system
 * @since 2019-02-16
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseService<UserWriter, UserReader, User>
    implements IUserService {

  @Resource private ReadMapper<User> userReadMapper;

  @Override
  public List<User> selectUserByGroupId(String groupId) {
    return this.readMapper.selectAll();
  }
}
