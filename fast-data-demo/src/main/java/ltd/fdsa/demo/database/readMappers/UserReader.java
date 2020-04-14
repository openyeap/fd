package ltd.fdsa.demo.database.readMappers;
 
import org.apache.ibatis.annotations.Mapper;

import ltd.fdsa.demo.model.entity.User;
import ltd.fdsa.starter.jdbc.ReadMapper;
//@Mapper
public interface UserReader extends ReadMapper<User> {

}
