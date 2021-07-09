package ltd.fdsa.job.admin.jpa.repository.writer;

import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
import ltd.fdsa.job.admin.jpa.entity.JobLog;
import ltd.fdsa.job.admin.jpa.entity.JobLogReport;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogReportWriter extends WriteRepository<JobLogReport, Integer> {
}
