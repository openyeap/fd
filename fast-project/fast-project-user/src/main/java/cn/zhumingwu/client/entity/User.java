package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 用户，作为系统的使用者
 * uid - 主键
 * name - 姓名
 * username - 用户名
 * password - 加密的密码
 * nick_name - 用户昵称
 * mobile_phone - 手机号
 * email_address - 电子邮箱
 * type - 用户类型:_user=0,_support=1，_admin=100
 * gender - 用户性别（0未知 1男 2女）
 * language - 默认语言
 * time_zone - 默认时区
 * avatar_uri - 头像路径
 * birthday - 生日
 * is_lunar_birthday - 是否农历
 * grade - 等级：_crystal = 0,_sliver = 1,_golden = 2,_black = 3,
 * country - 国家
 * province - 省份
 * city - 城市
 * district - 地区
 * salt - 加密盐
 * login_ip - 最后登录_i_p
 * login_ts - 最后登录时间
 * login_failed - 登录失败次数
 * password_expires_in - 密码过期时间
 * is_locked - 是否被锁定
 * locked_time - 锁定时间
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_user
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class User {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("username") 
    private String username;
    @JsonProperty("password") 
    private String password;
    @JsonProperty("nick_name") 
    private String nickName;
    @JsonProperty("mobile_phone") 
    private String mobilePhone;
    @JsonProperty("email_address") 
    private String emailAddress;
    @JsonProperty("type") 
    private UserType type;
    @JsonProperty("gender") 
    private GenderType gender;
    @JsonProperty("language") 
    private String language;
    @JsonProperty("time_zone") 
    private Integer timeZone;
    @JsonProperty("avatar_uri") 
    private String avatarUri;
    @JsonProperty("birthday") 
    private Date birthday;
    @JsonProperty("is_lunar_birthday") 
    private Boolean isLunarBirthday;
    @JsonProperty("grade") 
    private GradeType grade;
    @JsonProperty("country") 
    private String country;
    @JsonProperty("province") 
    private String province;
    @JsonProperty("city") 
    private String city;
    @JsonProperty("district") 
    private String district;
    @JsonProperty("salt") 
    private String salt;
    @JsonProperty("login_ip") 
    private String loginIp;
    @JsonProperty("login_time") 
    private Date loginTime;
    @JsonProperty("login_failed_times") 
    private Integer loginFailedTimes;
    @JsonProperty("password_expires_in") 
    private Integer passwordExpiresIn;
    @JsonProperty("is_locked") 
    private Boolean isLocked;
    @JsonProperty("locked_time") 
    private Date lockedTime;
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
