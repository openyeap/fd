package cn.zhumingwu.dataswitch.admin.entity;

import lombok.Data;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity<ID> {
    /**
     * 名称
     */
    @Column(name = "name")
    @Order(0)
    private String name;

    /**
     * 类型
     */
    @Column(name = "type")
    @Order(0)
    private Byte type = 0;


    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @CreatedBy
    @Column(name = "create_by")
    private ID createBy;

    /**
     * 更新者
     */
    @LastModifiedBy
    @Column(name = "update_by")
    private ID updateBy;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort = 0;

    /**
     * 备注
     */
    @Column(name = "remark")
    @Order(1000)
    private String remark;

    /**
     * 状态
     */
    @Column(name = "status")
    @Order(1000)
    private Status status = Status.OK;

    public enum Status {

        /**
         * 正常状态码
         */

        OK(1, "正常"),
        /**
         * 冻结状态码
         */
        FREEZE(2, "冻结"),

        /**
         * 删除状态码
         */
        DELETE(4, "删除");

        private int code;

        private String message;

        Status(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

}
