package ltd.fdsa.job.admin.jpa.repository.reader;


import ltd.fdsa.database.jpa.repository.reader.ReadRepository;
import ltd.fdsa.job.admin.jpa.entity.JobLogReport;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogReportReader extends ReadRepository<JobLogReport, Integer> {
}
