package ltd.fdsa.database.service;


import ltd.fdsa.database.entity.BaseEntity;
import ltd.fdsa.database.repository.IRead;
import ltd.fdsa.database.repository.IWrite;

public interface DataAccessService<Entity extends BaseEntity<ID>, ID> extends IRead<Entity, ID>, IWrite<Entity, ID> {
}
