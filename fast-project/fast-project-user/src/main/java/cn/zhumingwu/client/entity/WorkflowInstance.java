package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 工作流实例
 * workflow_instance_id - 主键
 * cid - 关联到客户
 * workflow_id - 关联到工作流
 * action_id - 工作流状态:当前执行
 * content - 工作流的数据（json)
 * remark - 备注
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_workflow_instance
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class WorkflowInstance {

    @JsonProperty("instance_id") 
    private Integer instanceId;
    @JsonProperty("client_id") 
    private Long clientId;
    @JsonProperty("workflow_id") 
    private Integer workflowId;
    @JsonProperty("action_id") 
    private Integer actionId;
    @JsonProperty("content") 
    private Json content;
    @JsonProperty("remark") 
    private String remark;
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
