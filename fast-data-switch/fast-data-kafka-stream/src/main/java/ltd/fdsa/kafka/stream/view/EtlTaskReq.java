package ltd.fdsa.kafka.stream.view;

import lombok.Data;

@Data
public class EtlTaskReq {

    /**
     * 任务名
     */
    private String taskName;
    private int limit;
    private int offset;
}
