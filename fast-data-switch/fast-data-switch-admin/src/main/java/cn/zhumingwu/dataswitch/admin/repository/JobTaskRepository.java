package cn.zhumingwu.dataswitch.admin.repository;


import cn.zhumingwu.dataswitch.admin.entity.JobTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTaskRepository extends JpaRepository<JobTask, Long> {

}

