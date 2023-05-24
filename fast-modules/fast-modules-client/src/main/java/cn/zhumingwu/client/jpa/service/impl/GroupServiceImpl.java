package cn.zhumingwu.client.jpa.service.impl;

import cn.zhumingwu.client.jpa.entity.Group;
import cn.zhumingwu.database.jpa.service.BaseJpaService;
import cn.zhumingwu.client.jpa.repository.reader.GroupReader;
import cn.zhumingwu.client.jpa.repository.writer.GroupWriter;
import cn.zhumingwu.client.jpa.service.GroupService;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends BaseJpaService<Group, Integer, GroupWriter, GroupReader> implements GroupService {

}
