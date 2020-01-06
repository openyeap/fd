package ltd.fdsa.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ltd.fdsa.job.admin.core.model.JobGroup;

import java.util.List;


@Mapper
public interface JobGroupDao {

    public List<JobGroup> findAll();

    public List<JobGroup> findByAddressType(@Param("addressType") int addressType);

    public int save(JobGroup JobGroup);

    public int update(JobGroup JobGroup);

    public int remove(@Param("id") int id);

    public JobGroup load(@Param("id") int id);
}
