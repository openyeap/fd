package cn.zhumingwu.client.entity;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.zhumingwu.client.enums.*;

/**
 * 系统用户对应租户
 * user_client_id - 主键
 * cid - 关联到客户
 * uid - 关联到用户
 * @link table t_user_client
 * @author zhumingwu
 * @since 2023-08-04
 **/
@Data
public class UserClient {

    @JsonProperty("id") 
    private Long id;
    @JsonProperty("client_id") 
    private Long clientId;
    @JsonProperty("user_id") 
    private Long userId;
}
