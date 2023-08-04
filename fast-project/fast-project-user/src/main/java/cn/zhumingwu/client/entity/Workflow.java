package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 定义工作流的主表
 * workflow_id - 主键
 * cid - 关联到客户
 * name - 名称
 * remark - 描述
 * use_workflow - 是否启用工作流
 * allow_user - 发起人配置
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_workflow
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class Workflow {

    @JsonProperty("workflow_id") 
    private Integer workflowId;
    @JsonProperty("client_id") 
    private Long clientId;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("remark") 
    private String remark;
    @JsonProperty("use_workflow") 
    private Boolean useWorkflow;
    @JsonProperty("allow_user") 
    private Json allowUser;
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
