package ltd.fdsa.job.admin.dao;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.job.admin.jpa.entity.JobInfo;
import ltd.fdsa.job.admin.jpa.service.JobInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobInfoDaoTest {

    @Resource
    private JobInfoService JobInfoDao;

    @Test
    public void pageList() {
//        List<JobInfo> list = JobInfoDao.pageList(0, 20, 0, -1, null, null, null);
//        int list_count = JobInfoDao.pageListCount(0, 20, 0, -1, null, null, null);
//
//        log.info("{}", list);
//        log.info("{}", list_count);
//
//        List<JobInfo> list2 = JobInfoDao.getJobsByGroup(1);
    }

    @Test
    public void save_load() {
        JobInfo info = new JobInfo();
        info.setGroupId(1);
        info.setCronExpression("jobCron");
        info.setRemark("desc");
        info.setAuthor("setAuthor");
        info.setAlarmEmail("setAlarmEmail");
        info.setExecutorRouteStrategy("setExecutorRouteStrategy");
        info.setExecutorHandler("setExecutorHandler");
        info.setExecutorParam("setExecutorParam");
        info.setExecutorBlockStrategy("setExecutorBlockStrategy");

        info.setChildJobId("1");

  JobInfoDao.update(info);

        JobInfo info2 = JobInfoDao.findById(info.getId()).get();
        info2.setCronExpression("jobCron2");
        info2.setRemark("desc2");
        info2.setAuthor("setAuthor2");
        info2.setAlarmEmail("setAlarmEmail2");
        info2.setExecutorRouteStrategy("setExecutorRouteStrategy2");
        info2.setExecutorHandler("setExecutorHandler2");
        info2.setExecutorParam("setExecutorParam2");
        info2.setExecutorBlockStrategy("setExecutorBlockStrategy2");
        info2.setChildJobId("1");

//          item2 = JobInfoDao.update(info2);
//
//        JobInfoDao.delete(info2.getId());

//        List<JobInfo> list2 = JobInfoDao.getJobsByGroup(1);
//
//        int ret3 = JobInfoDao.findAllCount();
    }
}
