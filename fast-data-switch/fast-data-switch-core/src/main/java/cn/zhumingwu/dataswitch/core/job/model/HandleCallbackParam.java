package cn.zhumingwu.dataswitch.core.job.model;

import cn.zhumingwu.base.model.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HandleCallbackParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private Long logId;
    private long logDateTim;

    private Result<Object> executeResult;

}
