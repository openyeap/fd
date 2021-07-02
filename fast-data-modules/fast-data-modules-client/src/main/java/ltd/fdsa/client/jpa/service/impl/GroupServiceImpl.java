package ltd.fdsa.client.jpa.service.impl;

import ltd.fdsa.client.jpa.entity.Group;
import ltd.fdsa.client.jpa.service.GroupService;
import ltd.fdsa.database.jpa.service.BaseJpaService;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends BaseJpaService<Group, Integer> implements GroupService {

}
