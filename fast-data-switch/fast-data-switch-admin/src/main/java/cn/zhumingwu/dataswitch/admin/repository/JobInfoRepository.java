package cn.zhumingwu.dataswitch.admin.repository;


import cn.zhumingwu.dataswitch.admin.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, Integer> {
}
