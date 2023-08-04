package cn.zhumingwu.data.hub.admin.dao;

import cn.zhumingwu.data.hub.admin.entity.JobLog;
import cn.zhumingwu.dataswitch.admin.repository.JobLogRepository;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.annotation.Resource;
import java.util.Date;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobLogDaoTest {

    @Resource
    private JobLogRepository jobLogRepository;

    @Test
    public void test() {
//        List<JobLog> list = JobLogDao.pageList(0, 10, 1, 1, null, null, 1);
//        int list_count = JobLogDao.pageListCount(0, 10, 1, 1, null, null, 1);

        JobLog log = new JobLog();
        log.setJobGroup(1);
        log.setJobId(1);
        jobLogRepository.save(log);
        JobLog dto = jobLogRepository.findById(log.getId()).get();

        log.setTriggerTime(new Date());
        log.setTriggerCode(1);
        log.setTriggerMsg("1");
        log.setExecutorAddress("1");
        log.setExecutorHandler("1");
        log.setExecutorParam("1");
//        ret1 = JobLogDao.updateTriggerInfo(log);
//        dto = JobLogDao.load(log.getId());

        log.setHandleTime(new Date());
        log.setHandleCode(2);
        log.setHandleMsg("2");
//        ret1 = JobLogDao.updateHandleInfo(log);
//        dto = JobLogDao.load(log.getId());

    }
}
