package cn.zhumingwu.dataswitch.core.job.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class LogResult implements Serializable {
    private static final long serialVersionUID = 42L;
    private int fromLineNum;
    private int toLineNum;
    private String logContent;
    private long timestamp;
    private boolean isEnd;

    public LogResult(int fromLineNum, int toLineNum, String logContent, boolean isEnd) {
        this.fromLineNum = fromLineNum;
        this.toLineNum = toLineNum;
        this.logContent = logContent;
        this.isEnd = isEnd;
        this.timestamp = System.currentTimeMillis();
    }
}
