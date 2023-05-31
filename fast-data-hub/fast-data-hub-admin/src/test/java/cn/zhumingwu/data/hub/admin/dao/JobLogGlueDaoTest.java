//package cn.zhumingwu.job.admin.dao;
//
//import cn.zhumingwu.job.jpa.entity.JobLogGlue;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class JobLogGlueDaoTest {
//
//    @Resource
//    private JobLogGlueSe JobLogGlueDao;
//
//    @Test
//    public void test() {
//        JobLogGlue logGlue = new JobLogGlue();
//        logGlue.setJobId(1);
//        logGlue.setGlueType("1");
//        logGlue.setGlueSource("1");
//        logGlue.setGlueRemark("1");
//        int ret = JobLogGlueDao.save(logGlue);
//
//        List<JobLogGlue> list = JobLogGlueDao.findByJobId(1);
//
//        int ret2 = JobLogGlueDao.removeOld(1, 1);
//
//        int ret3 = JobLogGlueDao.deleteByJobId(1);
//    }
//}
