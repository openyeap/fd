package ltd.fdsa.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ltd.fdsa.job.admin.core.model.JobLogReport;

import java.util.Date;
import java.util.List;

/**
 * job log
 */
@Mapper
public interface JobLogReportDao {

	public int save(JobLogReport JobLogReport);

	public int update(JobLogReport JobLogReport);

	public List<JobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
												@Param("triggerDayTo") Date triggerDayTo);

	public JobLogReport queryLogReportTotal();

}
