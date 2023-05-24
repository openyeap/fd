package cn.zhumingwu.database.service;


import cn.zhumingwu.database.repository.IRead;
import cn.zhumingwu.database.repository.IWrite;
import cn.zhumingwu.database.entity.BaseEntity;

public interface DataAccessService<Entity extends BaseEntity<ID>, ID> extends IRead<Entity, ID>, IWrite<Entity, ID> {
}
