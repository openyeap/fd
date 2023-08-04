package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 客户，作为我们服务的群体。既可以是个体，也可以是群体
 * cid - 主键
 * uid - 关联到某一个用户
 * name - 名称
 * phone_number - 联系电话
 * email_address - 电子邮箱
 * type - 客户类型:_unknown,_client,_company,_government
 * client_code - 授权使用的应用编号对应app_id
 * client_secret - 授权使用的应用密钥对应app_secret
 * allow_scopes - 允许访问的范围
 * grant_type - 允许授权方式:_implicit,_hybrid,_authorization_code,_client_credentials,_resource_owner_password,_device_flow
 * token_lifetime - 令牌有效期，单位秒
 * access_token_lifetime - access_token有效期，单位秒
 * authorization_code_lifetime - 授权码有效期，单位秒
 * refresh_token_lifetime - refresh_token有效期，单位秒
 * claim_prefix - 前缀
 * issue_ts - 发行时间
 * expires_in - 过期时间
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_client
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class Client {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("user_id") 
    private Long userId;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("phone_number") 
    private String phoneNumber;
    @JsonProperty("email_address") 
    private String emailAddress;
    @JsonProperty("type") 
    private ClientType type;
    @JsonProperty("client_code") 
    private String clientCode;
    @JsonProperty("client_secret") 
    private String clientSecret;
    @JsonProperty("allow_scopes") 
    private String allowScopes;
    @JsonProperty("grant") 
    private GrantType grant;
    @JsonProperty("token_lifetime") 
    private Long tokenLifetime;
    @JsonProperty("access_token_lifetime") 
    private Long accessTokenLifetime;
    @JsonProperty("authorization_code_lifetime") 
    private Long authorizationCodeLifetime;
    @JsonProperty("refresh_token_lifetime") 
    private Long refreshTokenLifetime;
    @JsonProperty("claim_prefix") 
    private String claimPrefix;
    @JsonProperty("issue_time") 
    private Date issueTime;
    @JsonProperty("expires_in") 
    private Long expiresIn;
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
