package cn.zhumingwu.dataswitch.core.job.model;

import cn.zhumingwu.dataswitch.core.job.enums.ExecutorBlockStrategy;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class TriggerParam implements Serializable {
    private static final long serialVersionUID = 42L;
    private Long jobId;
    private Long taskId;
    private String handler;
    private List<String> params;
    private ExecutorBlockStrategy executorBlockStrategy;
    private int timeout;
    private int retryTimes;
    private long timestamp;
}
