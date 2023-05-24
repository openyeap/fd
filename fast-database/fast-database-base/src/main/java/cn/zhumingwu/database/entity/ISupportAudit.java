package cn.zhumingwu.database.entity;

import java.time.LocalDateTime;

public interface ISupportAudit<ID> {
    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();

    ID getCreateBy();

    ID getUpdateBy();

    void setCreateTime(LocalDateTime createTime);

    void setUpdateTime(LocalDateTime updateTime);

    void setCreateBy(ID createBy);

    void setUpdateBy(ID updateBy);
}
