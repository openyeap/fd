package cn.zhumingwu.dataswitch.admin.repository;


import cn.zhumingwu.dataswitch.admin.entity.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Integer> {
}
