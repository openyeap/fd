package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 组织人员表
 * staff_id - 主键
 * uid - 关联的用户_id
 * name - 姓名
 * description - 描述
 * sort - 顺序
 * department_id - 关联到部门
 * leader_user_id - 直接领导
 * phone - 联系电话
 * email - 邮箱
 * title - 岗位
 * cid - 主键
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_client_staff
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class ClientStaff {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("user_id") 
    private Long userId;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("description") 
    private String description;
    @JsonProperty("sort_no") 
    private Integer sortNo;
    @JsonProperty("department_id") 
    private Long departmentId;
    @JsonProperty("leader_id") 
    private Long leaderId;
    @JsonProperty("phone") 
    private String phone;
    @JsonProperty("email") 
    private String email;
    @JsonProperty("title") 
    private String title;
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
