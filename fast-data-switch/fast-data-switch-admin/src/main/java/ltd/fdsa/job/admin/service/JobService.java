//package ltd.fdsa.job.admin.service;
//
//import ltd.fdsa.job.admin.jpa.entity.JobInfo;
//import ltd.fdsa.web.view.Result;
//
//import java.util.Date;
//import java.util.Map;
//
///**
// *
// */
//public interface JobService {
//
//    /**
//     * page list
//     *
//     * @param start
//     * @param length
//     * @param jobGroup
//     * @param jobDesc
//     * @param executorHandler
//     * @param author
//     * @return
//     */
//    public Map<String, Object> pageList(
//            int start,
//            int length,
//            int jobGroup,
//            int triggerStatus,
//            String jobDesc,
//            String executorHandler,
//            String author);
//
//    /**
//     * add job
//     *
//     * @param jobInfo
//     * @return
//     */
//    public Result<String> add(JobInfo jobInfo);
//
//    /**
//     * update job
//     *
//     * @param jobInfo
//     * @return
//     */
//    public Result<String> update(JobInfo jobInfo);
//
//    /**
//     * remove job *
//     *
//     * @param id
//     * @return
//     */
//    public Result<String> remove(int id);
//
//    /**
//     * start job
//     *
//     * @param id
//     * @return
//     */
//    public Result<String> start(int id);
//
//    /**
//     * stop job
//     *
//     * @param id
//     * @return
//     */
//    public Result<String> stop(int id);
//
//    /**
//     * dashboard info
//     *
//     * @return
//     */
//    public Map<String, Object> dashboardInfo();
//
//    /**
//     * chart info
//     *
//     * @param startDate
//     * @param endDate
//     * @return
//     */
//    public Result<Map<String, Object>> chartInfo(Date startDate, Date endDate);
//}
