package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 数据集
 * dataset_id - 主键
 * name - 名称
 * description - 应用描述
 * tags - 自定义标签列表
 * types - 自定义类型列表
 * scenes - 自定义场景列表
 * cid - 主键
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_dataset
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class Dataset {

    @JsonProperty("id") 
    private String id;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("description") 
    private String description;
    @JsonProperty("tags") 
    private String tags;
    @JsonProperty("types") 
    private String types;
    @JsonProperty("scenes") 
    private String scenes;
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
