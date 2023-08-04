package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 连接供应商配置
 * connect_id - 主键
 * name - 平台名称
 * description - 平台描述
 * logo_uri - 平台_logo，用于展示登录界面
 * app_key - 授权使用的应用编号对应app_id
 * app_secret - 授权使用的应用密钥对应app_secret
 * scope - 平台的范围
 * request_uri - 请求地址，支持占位
 * redirect_uri - 返回地址，支持占位
 * response_type - 返回类型，基本固定：code
 * access_token_uri - 请求access token的地址，支持占位
 * grant_type - 请求access token的授权类型，基本固定：authorization_code
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_connect_provider
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class ConnectProvider {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("description") 
    private String description;
    @JsonProperty("logo_uri") 
    private String logoUri;
    @JsonProperty("client_code") 
    private String clientCode;
    @JsonProperty("client_secret") 
    private String clientSecret;
    @JsonProperty("scope") 
    private String scope;
    @JsonProperty("request_uri") 
    private String requestUri;
    @JsonProperty("redirect_uri") 
    private String redirectUri;
    @JsonProperty("response_type") 
    private String responseType;
    @JsonProperty("access_token_uri") 
    private String accessTokenUri;
    @JsonProperty("grant_type") 
    private String grantType;
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
