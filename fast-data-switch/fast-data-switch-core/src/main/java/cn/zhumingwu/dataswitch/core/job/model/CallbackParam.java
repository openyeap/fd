package cn.zhumingwu.dataswitch.core.job.model;

import lombok.*;


@ToString
@Setter
@Getter
public class CallbackParam extends LogResult {

    private Long jobId;
    private Long taskId;
    private String handler;

    public CallbackParam(
            Long jobId,
            Long taskId,
            String handler, int fromLineNum, int toLineNum, String logContent, boolean isEnd) {
        super(fromLineNum, toLineNum, logContent, isEnd);
        this.jobId = jobId;
        this.taskId = taskId;
        this.handler = handler;
    }
}
