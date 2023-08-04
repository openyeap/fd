package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 连接授权信息
 * user_connect_id - 主键
 * uid - 关联连接
 * connect_provider_id - 关联连接
 * access_token - 平台授权的访问令牌
 * refresh_token - 平台刷新令牌
 * expires_in - access _token过期时间，通常为秒
 * refresh_expires_in - refresh _token过期时间，通常为秒
 * issue_ts - 平台授权的时间，用于计算是否过期
 * open_id - 平台上的用户id
 * union_id - 平台上的用户联合id
 * user_nick_name - 平台上的用户显示名
 * extensions - 平台的其它信息
 * scopes - 平台授权的访问范围
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_user_connect
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class UserConnect {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("user_id") 
    private Long userId;
    @JsonProperty("connect_provider_id") 
    private Long connectProviderId;
    @JsonProperty("access_token") 
    private String accessToken;
    @JsonProperty("refresh_token") 
    private String refreshToken;
    @JsonProperty("expires_in") 
    private Long expiresIn;
    @JsonProperty("refresh_expires_in") 
    private Long refreshExpiresIn;
    @JsonProperty("issue_time") 
    private Date issueTime;
    @JsonProperty("open_id") 
    private String openId;
    @JsonProperty("union_id") 
    private String unionId;
    @JsonProperty("user_nick_name") 
    private String userNickName;
    @JsonProperty("extensions") 
    private String extensions;
    @JsonProperty("scopes") 
    private String scopes;
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
