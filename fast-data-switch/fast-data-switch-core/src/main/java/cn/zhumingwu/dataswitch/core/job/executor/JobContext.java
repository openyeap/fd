package cn.zhumingwu.dataswitch.core.job.executor;

import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import cn.zhumingwu.dataswitch.core.util.JobFileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.MessageFormat;
import java.util.List;

/**
 * job context
 *
 * @author zhumingwu
 */
public class JobContext {
    private static final Logger logger = LoggerFactory.getLogger(JobContext.class);
    private static final InheritableThreadLocal<JobContext> contextHolder = new InheritableThreadLocal<JobContext>(); // support for child thread of job handler)

    public static void setJobContext(TriggerParam param) {
        contextHolder.set(new JobContext(param));
    }

    public static JobContext getJobContext() {
        return contextHolder.get();
    }

    private final TriggerParam param;


    // ---------------------- for handle ----------------------

    /**
     * handleCode：The result status of job execution
     * <p>
     * 200 : success
     * 500 : fail
     * 502 : timeout
     */
    private int handleCode;

    /**
     * handleMsg：The simple log msg of job execution
     */
    private String handleMsg;

    private static String BasePath = "/logs/job";

    private String logFileName;

    public JobContext(TriggerParam param) {
        this.param = param;
        this.logFileName = JobFileAppender.getLogFile(this.getHandler(), this.getJobId(), this.getTaskId(), this.getTimestamp());
        JobFileAppender.initLogDir(this.logFileName);
    }

    public String getHandler() {
        return this.param.getHandler();
    }

    public long getTimestamp() {
        return this.param.getTimestamp();
    }

    public long getTaskId() {
        return this.param.getTaskId();
    }

    public long getJobId() {
        return this.param.getJobId();
    }

    public List<String> getJobParam() {
        return this.param.getParams();
    }


    public boolean log(String appendLogPattern, Object... appendLogArguments) {

        String appendLog = MessageFormat.format(appendLogPattern, appendLogArguments);
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        return JobFileAppender.logDetail(this.logFileName, callInfo, appendLog);
    }

    public boolean log(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String appendLog = stringWriter.toString();
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        return JobFileAppender.logDetail(this.logFileName, callInfo, appendLog);
    }


    public void setHandleCode(int handleCode) {
        this.handleCode = handleCode;
    }

    public int getHandleCode() {
        return handleCode;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    public String getHandleMsg() {
        return handleMsg;
    }


}