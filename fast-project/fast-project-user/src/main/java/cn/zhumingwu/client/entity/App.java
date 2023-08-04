package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 系统应用
 * app_id - 主键
 * name - 名称
 * description - 应用描述
 * icon - 图标
 * code - 应用编码 _ 高度集成的应用使用code区分
 * type - 应用类型:_blank,_self,_iframe,_micro_web,_client,
 * url - 应用路径
 * vendor_id - 供应商
 * issue_ts - 发行时间
 * expires_in - 过期时间
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_system_app
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class App {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("description") 
    private String description;
    @JsonProperty("icon") 
    private String icon;
    @JsonProperty("code") 
    private String code;
    @JsonProperty("type") 
    private AppType type;
    @JsonProperty("url") 
    private String url;
    @JsonProperty("vendor_id") 
    private Long vendorId;
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
