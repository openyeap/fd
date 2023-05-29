package cn.zhumingwu.dataswitch.core.job.model;

import cn.zhumingwu.dataswitch.core.job.enums.JobStatus;
import lombok.*;

import java.io.Serializable;


@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
public class CallbackParam implements Serializable {
    private static final long serialVersionUID = 42L;
    private Long jobId;
    private Long taskId;
    private String instanceId;
    private JobStatus status;
    private int code;
    private String message;
    private long timestamp;

}
