package ltd.fdsa.client.mybatis.plus.service.impl;

import ltd.fdsa.client.mybatis.entity.Role;
import ltd.fdsa.client.mybatis.plus.mapper.reader.RoleReader;
import ltd.fdsa.client.mybatis.plus.mapper.writer.RoleWriter;
import ltd.fdsa.client.mybatis.plus.service.RoleService;
import ltd.fdsa.database.mybatis.plus.service.MybatisPlusService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends MybatisPlusService<Role, Integer, RoleWriter, RoleReader> implements RoleService {

}
