package cn.zhumingwu.dataswitch.admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogReportRepository extends JpaRepository<JobLogReport, Integer> {
}
