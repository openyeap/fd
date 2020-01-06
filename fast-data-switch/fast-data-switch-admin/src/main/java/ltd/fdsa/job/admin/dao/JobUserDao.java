package ltd.fdsa.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ltd.fdsa.job.admin.core.model.JobUser;

import java.util.List;


@Mapper
public interface JobUserDao {

	public List<JobUser> pageList(@Param("offset") int offset,
                                     @Param("pagesize") int pagesize,
                                     @Param("username") String username,
									 @Param("role") int role);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("username") String username,
							 @Param("role") int role);

	public JobUser loadByUserName(@Param("username") String username);

	public int save(JobUser JobUser);

	public int update(JobUser JobUser);
	
	public int delete(@Param("id") int id);

}
