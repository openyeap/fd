package ltd.fdsa.test.service.impl;

import ltd.fdsa.test.model.entity.UserGroup;
import ltd.fdsa.test.database.writeMappers.UserGroupWriter;
import ltd.fdsa.test.database.readMappers.UserGroupReader;
import ltd.fdsa.test.service.IUserGroupService;
import ltd.fdsa.starter.jdbc.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserGroupServiceImpl extends BaseService<UserGroupWriter,UserGroupReader, UserGroup> implements IUserGroupService {

}
