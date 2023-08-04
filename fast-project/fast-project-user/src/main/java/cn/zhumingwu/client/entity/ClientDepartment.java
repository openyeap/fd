package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 组织部门表
 * department_id - 主键
 * parent_id - 外键
 * path - 祖级路径/分隔
 * name - 部门名称
 * content - 内容
 * sort - 顺序
 * leader - 部门领导
 * phone - 联系电话
 * email - 邮箱
 * cid - 主键
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_client_department
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class ClientDepartment {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("parent_id") 
    private Long parentId;
    @JsonProperty("path") 
    private String path;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("content") 
    private String content;
    @JsonProperty("sort_no") 
    private Integer sortNo;
    @JsonProperty("leader") 
    private Long leader;
    @JsonProperty("phone") 
    private String phone;
    @JsonProperty("email") 
    private String email;
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
