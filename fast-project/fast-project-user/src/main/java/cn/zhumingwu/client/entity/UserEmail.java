package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 系统用户对应的电子邮箱
 * user_email_id - 主键
 * email_address - 电子邮箱
 * validate_code - 验证码，随邮件发放
 * validate_time - 验证码时间
 * confirm_time - 验证时间
 * expires_in - 过期时间
 * uid - 关联到用户
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_user_email
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class UserEmail {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("email_address") 
    private String emailAddress;
    @JsonProperty("validate_code") 
    private String validateCode;
    @JsonProperty("validate_time") 
    private Date validateTime;
    @JsonProperty("confirmed_time") 
    private Date confirmedTime;
    @JsonProperty("expires_in") 
    private Long expiresIn;
    @JsonProperty("user_id") 
    private Long userId;
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
