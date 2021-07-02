package ltd.fdsa.client.mybatis.generic.service.impl;

import ltd.fdsa.client.mybatis.entity.User;
import ltd.fdsa.client.mybatis.generic.mapper.reader.UserReader;
import ltd.fdsa.client.mybatis.generic.mapper.writer.UserWriter;
import ltd.fdsa.client.mybatis.generic.service.UserService;
import ltd.fdsa.database.mybatis.generic.service.MybatisGenericService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends MybatisGenericService<User, Integer, UserWriter, UserReader> implements UserService {

}
