package ltd.fdsa.database.repository;


import ltd.fdsa.database.entity.BaseEntity;

public interface IWrite<Entity extends BaseEntity, ID> {
    Entity update(Entity entity);

    void updateAll(Entity... entities);

    void deleteById(ID id);

    void deleteAll(Entity... entities);

    void clearAll();

    Entity insert(Entity entity);

    void insertAll(Entity... entities);

    default int executeSql(String sql) {
        return 0;
    }
}