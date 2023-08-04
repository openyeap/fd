package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 数据集数据属性
 * attribute_id - 主键
 * dataset_id - 主键
 * blob_id - 主键，基于内容的hash and sha1
 * path - 文件路径，如果以/结尾这目录
 * type - 类型，文件的扩展信息
 * size - 文件大小
 * width - 图片宽度
 * height - 图片高度
 * tags - 自定义标签
 * types - 自定义数据类型
 * scenes - 自定义数据场景
 * cid - 主键
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_dataset_attribute
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class DatasetAttribute {

    @JsonProperty("id") 
    private String id;
    @JsonProperty("dataset_id") 
    private String datasetId;
    @JsonProperty("file_id") 
    private String fileId;
    @JsonProperty("path") 
    private String path;
    @JsonProperty("type") 
    private String type;
    @JsonProperty("size") 
    private Long size;
    @JsonProperty("width") 
    private Integer width;
    @JsonProperty("height") 
    private Integer height;
    @JsonProperty("tags") 
    private Long tags;
    @JsonProperty("types") 
    private Long types;
    @JsonProperty("scenes") 
    private Long scenes;
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
