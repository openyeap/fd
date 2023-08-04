package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 系统用户对应的角色
 * staff_role_id - 主键
 * staff_id - staff_id
 * role_id - role_id
 * cid - 主键
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_client_staff_role
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class ClientStaffRole {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("staff_id") 
    private Long staffId;
    @JsonProperty("role_id") 
    private Long roleId;
    @JsonProperty("cid") 
    private Integer cid;
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
