package cn.zhumingwu.client.mybatis.generic.service.impl;

import cn.zhumingwu.client.mybatis.entity.User;
import cn.zhumingwu.client.mybatis.generic.mapper.reader.UserReader;
import cn.zhumingwu.client.mybatis.generic.mapper.writer.UserWriter;
import cn.zhumingwu.client.mybatis.generic.service.UserService;
import cn.zhumingwu.database.mybatis.generic.service.MybatisGenericService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends MybatisGenericService<User, Integer, UserWriter, UserReader> implements UserService {

}
