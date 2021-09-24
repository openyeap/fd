package ltd.fdsa.client.service.impl;

import ltd.fdsa.client.jpa.entity.User;
import ltd.fdsa.client.jpa.repository.reader.UserReader;
import ltd.fdsa.client.jpa.repository.writer.UserWriter;
import ltd.fdsa.client.jpa.service.UserService;
import ltd.fdsa.database.jpa.service.BaseJpaService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseJpaService<User, Integer, UserWriter, UserReader> implements UserService {

}
