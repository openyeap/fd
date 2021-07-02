package ltd.fdsa.switcher.core.job.executor;

import ltd.fdsa.switcher.core.job.model.LogResult;
import ltd.fdsa.web.view.Result;
import java.util.Map;

public interface Executor {

    /**
     * coordinators send head beat
     *
     * @return
     */
    public Result<String> beat();

    /**
     * idle beat for job
     *
     * @param jobId
     * @return
     */
    public Result<String> idleBeat(int jobId);

    /**
     * coordinators send kill to executor
     *
     * @param jobId
     * @return
     */
    public Result<String> kill(int jobId);

    /**
     * coordinators get job's log
     *
     * @param jobId
     * @param lastVersion
     * @return
     */
    public Result<LogResult> log(int jobId, String lastVersion);

    /**
     * coordinators send a job run to executor
     *
     * @return
     */
    public Result<String> run(int jobId, String handler, String blockStrategy, long timeout, Map<String, String> content);
}
