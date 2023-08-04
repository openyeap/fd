package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 数据集数据标注
 * dataset_annotation_id - 主键
 * dataset_id - 主键
 * blob_id - 基于内容的hash and sha1
 * name - 标注名称，使用/表示层次
 * type - 类型，line,circle,rectangle,ellipse,polygon
 * index - 数据位置
 * top - 标注位置
 * left - 标注位置
 * width - 图片宽度
 * height - 图片高度
 * segmented - 是否分割
 * is_keypoint - 是否关键点
 * cid - 主键
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_dataset_annotation
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class DatasetAnnotation {

    @JsonProperty("id") 
    private String id;
    @JsonProperty("dataset_id") 
    private String datasetId;
    @JsonProperty("file_id") 
    private String fileId;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("type") 
    private String type;
    @JsonProperty("index") 
    private Integer index;
    @JsonProperty("top") 
    private Long top;
    @JsonProperty("left") 
    private Long left;
    @JsonProperty("width") 
    private Integer width;
    @JsonProperty("height") 
    private Integer height;
    @JsonProperty("segmented") 
    private Boolean segmented;
    @JsonProperty("is_keypoint") 
    private Boolean isKeypoint;
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
