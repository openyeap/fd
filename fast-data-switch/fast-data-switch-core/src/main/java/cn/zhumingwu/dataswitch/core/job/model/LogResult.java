package cn.zhumingwu.dataswitch.core.job.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
public class LogResult implements Serializable {
    private static final long serialVersionUID = 42L;
    private int fromLineNum;
    private int toLineNum;
    private String logContent;
    private boolean isEnd;

    public LogResult(int fromLineNum, int toLineNum, String logContent, boolean isEnd) {
        this.fromLineNum = fromLineNum;
        this.toLineNum = toLineNum;
        this.logContent = logContent;
        this.isEnd = isEnd;
    }
}
