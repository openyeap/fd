package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 定义工作流的主表
 * form_id - 主键
 * workflow_id - 关联工作流表
 * cid - 关联到客户
 * name - 名称
 * code - 编号,可以为名称
 * remark - 备注
 * type - 类型
 * sort - 顺序
 * min - 长度min
 * max - 长度max
 * default - default
 * pattern - 规范，系统内置的类型或正则
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_workflow_form
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class WorkflowForm {

    @JsonProperty("form_id") 
    private Integer formId;
    @JsonProperty("workflow_id") 
    private Integer workflowId;
    @JsonProperty("client_id") 
    private Long clientId;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("code") 
    private String code;
    @JsonProperty("remark") 
    private String remark;
    @JsonProperty("type") 
    private String type;
    @JsonProperty("sort_no") 
    private Integer sortNo;
    @JsonProperty("min") 
    private Integer min;
    @JsonProperty("max") 
    private Integer max;
    @JsonProperty("default_value") 
    private String defaultValue;
    @JsonProperty("pattern") 
    private String pattern;
    @JsonProperty("created_time") 
    private Date createdTime;
    @JsonProperty("updated_time") 
    private Date updatedTime;
    @JsonProperty("created_by") 
    private Long createdBy;
    @JsonProperty("updated_by") 
    private Long updatedBy;
    @JsonProperty("status") 
    private Status status;
}
