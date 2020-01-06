package ltd.fdsa.job.admin.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import ltd.fdsa.job.admin.core.model.JobLog;
import ltd.fdsa.job.admin.dao.JobLogDao;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobLogDaoTest {

    @Resource
    private JobLogDao JobLogDao;

    @Test
    public void test(){
        List<JobLog> list = JobLogDao.pageList(0, 10, 1, 1, null, null, 1);
        int list_count = JobLogDao.pageListCount(0, 10, 1, 1, null, null, 1);

        JobLog log = new JobLog();
        log.setJobGroup(1);
        log.setJobId(1);

        long ret1 = JobLogDao.save(log);
        JobLog dto = JobLogDao.load(log.getId());

        log.setTriggerTime(new Date());
        log.setTriggerCode(1);
        log.setTriggerMsg("1");
        log.setExecutorAddress("1");
        log.setExecutorHandler("1");
        log.setExecutorParam("1");
        ret1 = JobLogDao.updateTriggerInfo(log);
        dto = JobLogDao.load(log.getId());


        log.setHandleTime(new Date());
        log.setHandleCode(2);
        log.setHandleMsg("2");
        ret1 = JobLogDao.updateHandleInfo(log);
        dto = JobLogDao.load(log.getId());


        List<Map<String, Object>> list2 = JobLogDao.triggerCountByDay(new Date(new Date().getTime() + 30*24*60*60*1000), new Date());

        int ret4 = JobLogDao.clearLog(1, 1, new Date(), 100);

        int ret2 = JobLogDao.delete(log.getJobId());

        int ret3 = JobLogDao.triggerCountByHandleCode(-1);
    }

}
