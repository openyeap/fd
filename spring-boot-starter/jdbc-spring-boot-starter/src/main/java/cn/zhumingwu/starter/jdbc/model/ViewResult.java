package cn.zhumingwu.starter.jdbc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ViewResult {
    private String name;
    private Object content;
    private String remark;
}
