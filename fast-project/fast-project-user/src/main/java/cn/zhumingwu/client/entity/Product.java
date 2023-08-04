package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 产品信息
 * product_id - 主键
 * cid - 所属客户
 * parent_id - 表示_s_p_u,其它表示_s_k_u
 * code - 编码/款号/条码
 * title - 标题
 * summary - 简述
 * detail - 详述
 * imges - 图片列表
 * videos - 视频列表
 * unit - 单位
 * category - 类目
 * geometry - 位置
 * from_time - 有效期
 * end_time - 保质期
 * create_time - 创建时间
 * update_time - 更新时间
 * create_by - 创建用户
 * update_by - 更新用户
 * status - 状态:0_in_active,1_active,_1_deleted
 * @link table t_product
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class Product {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("client_id") 
    private Long clientId;
    @JsonProperty("parent_id") 
    private Long parentId;
    @JsonProperty("code") 
    private String code;
    @JsonProperty("name") 
    private String name;
    @JsonProperty("summary") 
    private String summary;
    @JsonProperty("detail") 
    private String detail;
    @JsonProperty("images") 
    private String images;
    @JsonProperty("videos") 
    private String videos;
    @JsonProperty("unit") 
    private String unit;
    @JsonProperty("category") 
    private String category;
    @JsonProperty("geometry") 
    private String geometry;
    @JsonProperty("from_time") 
    private String fromTime;
    @JsonProperty("end_time") 
    private String endTime;
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
