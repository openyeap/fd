package ltd.fdsa.demo.database.writeMappers;
 

import org.apache.ibatis.annotations.Mapper;

import ltd.fdsa.demo.model.entity.User;
import ltd.fdsa.starter.jdbc.WriteMapper;
//@Mapper
public interface UserWriter extends WriteMapper<User> {

}
