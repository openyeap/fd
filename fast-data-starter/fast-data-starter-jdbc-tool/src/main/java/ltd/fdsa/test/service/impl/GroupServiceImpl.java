package ltd.fdsa.test.service.impl;

import ltd.fdsa.test.model.entity.Group;
import ltd.fdsa.test.database.writeMappers.GroupWriter;
import ltd.fdsa.test.database.readMappers.GroupReader;
import ltd.fdsa.test.service.IGroupService;
import ltd.fdsa.starter.jdbc.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends BaseService<GroupWriter,GroupReader, Group> implements IGroupService {

}
