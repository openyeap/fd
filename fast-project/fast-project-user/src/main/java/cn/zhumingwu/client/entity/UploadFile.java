package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 文件表
 * upload_file_id - 主键
 * name - 名称
 * content - 内容
 * url - 图片
 * size - 大小
 * from_time - 有效始时
 * end_time - 有效终时
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_upload_file
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class UploadFile {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("content") 
    private String content;
    @JsonProperty("url") 
    private String url;
    @JsonProperty("size") 
    private String size;
    @JsonProperty("from_time") 
    private Date fromTime;
    @JsonProperty("end_time") 
    private Date endTime;
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
