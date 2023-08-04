package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 系统角色表
 * role_id - 主键
 * code - 名称 _ 系统生成,同一个客户下唯一，需要尽可能短小
 * name - 角色名称
 * description - 描述
 * cid - 主键
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_client_role
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class ClientRole {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("code") 
    private String code;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("description") 
    private String description;
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
