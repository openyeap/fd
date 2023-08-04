package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 客户的应用
 * client_app_id - 主键
 * app_id - 主键
 * app_code - 授权使用的应用编号对应app_id
 * app_secret - 授权使用的应用密钥对应app_secret
 * name - 名称
 * description - 应用描述
 * token_lifetime - 令牌有效期，单位秒
 * access_token_lifetime - access_token有效期，单位秒
 * authorization_code_lifetime - 授权码有效期，单位秒
 * refresh_token_lifetime - refresh_token有效期，单位秒
 * claim_prefix - 前缀
 * issue_ts - 发行时间
 * expires_in - 过期时间
 * cid - 主键
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_client_app
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class ClientApp {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("app_id") 
    private Long appId;
    @JsonProperty("app_code") 
    private String appCode;
    @JsonProperty("app_secret") 
    private String appSecret;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("description") 
    private String description;
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
