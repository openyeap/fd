package cn.zhumingwu.client.mybatis.plus.service.impl;

import cn.zhumingwu.client.mybatis.entity.Role;
import cn.zhumingwu.client.mybatis.plus.mapper.reader.RoleReader;
import cn.zhumingwu.client.mybatis.plus.mapper.writer.RoleWriter;
import cn.zhumingwu.client.mybatis.plus.service.RoleService;
import cn.zhumingwu.database.mybatis.plus.service.MybatisPlusService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends MybatisPlusService<Role, Integer, RoleWriter, RoleReader> implements RoleService {

}
