package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 产品类目
 * category_id - 主键
 * cid - 所属客户
 * parent_id - 祖类目
 * path - 祖级路径/分隔
 * level - 层级
 * name - 名称
 * icon - 图标
 * content - 内容
 * sort - 顺序
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_category
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class Category {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("client_id") 
    private Long clientId;
    @JsonProperty("parent_id") 
    private Long parentId;
    @JsonProperty("path") 
    private String path;
    @JsonProperty("level") 
    private Integer level;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("icon") 
    private String icon;
    @JsonProperty("content") 
    private String content;
    @JsonProperty("sort_no") 
    private Integer sortNo;
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
