package ltd.fdsa.test.service.impl;

import ltd.fdsa.test.model.entity.Role;
import ltd.fdsa.test.database.writeMappers.RoleWriter;
import ltd.fdsa.test.database.readMappers.RoleReader;
import ltd.fdsa.test.service.IRoleService;
import ltd.fdsa.starter.jdbc.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseService<RoleWriter,RoleReader, Role> implements IRoleService {

}
