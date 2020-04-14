package ltd.fdsa.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.demo.database.readMappers.UserReader;
import ltd.fdsa.demo.database.writeMappers.UserWriter;
import ltd.fdsa.demo.model.entity.*;
import ltd.fdsa.demo.model.view.ViewUser;
import ltd.fdsa.demo.service.IUserService;
import ltd.fdsa.starter.jdbc.ReadMapper;
import ltd.fdsa.starter.jdbc.service.BaseService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author system
 * @since 2019-02-16
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseService<UserWriter, UserReader, User> implements IUserService {

	@Resource
	private ReadMapper<User> userReadMapper;

	@Override
	public List<User> selectUserByGroupId(String groupId) {
		return this.readMapper.selectAll();
	}

}
