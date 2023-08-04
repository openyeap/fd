package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 张贴表
 * post_id - 主键
 * cid - 所属客户
 * category_path - 类目路径
 * title - 标题
 * data - 内容
 * image - 图片
 * geometry - 位置
 * from_time - 有效始时
 * end_time - 有效终时
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_post
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class Post {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("client_id") 
    private Long clientId;
    @JsonProperty("category_path") 
    private String categoryPath;
    @JsonProperty("title") 
    private String title;
    @JsonProperty("data") 
    private Json data;
    @JsonProperty("image") 
    private String image;
    @JsonProperty("geometry") 
    private String geometry;
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
