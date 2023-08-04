package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 定义工作流的主表
 * action_id - 主键
 * cid - 关联到客户
 * workflow_id - 关联到workflow
 * parent_action_id - 上一步，关联到工作流步骤
 * pre_condition - 进入条件，规则表达式
 * name - 名称
 * code - 编码，为了效率使用byte记录
 * finish_condition - 完成条件：1_任意人同意即可，2_两人同意即可，_1_所有人同意方可
 * allow_user - 发起人的配置
 * allow_actor - 参与人的配置
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_workflow_action
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class WorkflowAction {

    @JsonProperty("action_id") 
    private Integer actionId;
    @JsonProperty("client_id") 
    private Long clientId;
    @JsonProperty("workflow_id") 
    private Integer workflowId;
    @JsonProperty("parent_action_id") 
    private Integer parentActionId;
    @JsonProperty("pre_condition") 
    private String preCondition;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("code") 
    private String code;
    @JsonProperty("finish_condition") 
    private Integer finishCondition;
    @JsonProperty("allow_user") 
    private Json allowUser;
    @JsonProperty("allow_actor") 
    private Json allowActor;
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
