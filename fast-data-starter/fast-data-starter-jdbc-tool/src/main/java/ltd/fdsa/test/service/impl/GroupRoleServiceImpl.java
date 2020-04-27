package ltd.fdsa.test.service.impl;

import ltd.fdsa.test.model.entity.GroupRole;
import ltd.fdsa.test.database.writeMappers.GroupRoleWriter;
import ltd.fdsa.test.database.readMappers.GroupRoleReader;
import ltd.fdsa.test.service.IGroupRoleService;
import ltd.fdsa.starter.jdbc.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class GroupRoleServiceImpl extends BaseService<GroupRoleWriter,GroupRoleReader, GroupRole> implements IGroupRoleService {

}
