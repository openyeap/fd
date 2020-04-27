package ltd.fdsa.test.service.impl;

import ltd.fdsa.test.model.entity.User;
import ltd.fdsa.test.database.writeMappers.UserWriter;
import ltd.fdsa.test.database.readMappers.UserReader;
import ltd.fdsa.test.service.IUserService;
import ltd.fdsa.starter.jdbc.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseService<UserWriter,UserReader, User> implements IUserService {

}
