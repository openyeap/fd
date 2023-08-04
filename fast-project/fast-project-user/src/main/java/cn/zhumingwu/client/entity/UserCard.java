package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 系统用户的会员卡
 * user_card_id - 主键
 * code - 卡号
 * grade - 等级
 * name - 名称
 * validate_code - 验证码,与卡号一起发放
 * confirm_time - 验证时间
 * expires_in - 过期时间
 * uid - 关联到用户
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_user_card
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class UserCard {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("code") 
    private String code;
    @JsonProperty("grade") 
    private GradeType grade;
    @JsonProperty("name") 
    private String name;
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
