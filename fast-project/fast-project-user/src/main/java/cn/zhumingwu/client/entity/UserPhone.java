package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 系统用户的电话号码
 * user_phone_id - 主键
 * type - 类型
 * phone_number - 电话号码
 * validate_code - 验证码
 * confirm_time - 验证时间
 * expires_in - 过期时间
 * uid - 关联到用户
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_user_phone
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class UserPhone {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("type") 
    private PhoneType type;
    @JsonProperty("phone_number") 
    private String phoneNumber;
    @JsonProperty("validate_code") 
    private String validateCode;
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
