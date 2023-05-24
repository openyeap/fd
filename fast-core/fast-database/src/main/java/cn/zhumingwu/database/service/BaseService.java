package cn.zhumingwu.database.service;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.core.util.NamingUtils;
import cn.zhumingwu.database.config.DataSourceConfig;
import cn.zhumingwu.database.entity.BaseEntity;
import cn.zhumingwu.database.entity.Status;
import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.functions.Function;
import cn.zhumingwu.database.sql.queries.Queries;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.utils.PlaceHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSetMetaData;
import java.util.*;

@Slf4j
public class BaseService<Entity extends BaseEntity<ID>, ID> implements DataAccessService<Entity, ID> {

    @Resource(name = DataSourceConfig.WRITER_DATASOURCE)
    protected DataSource writer;

    @Resource(name = DataSourceConfig.READER_DATASOURCE)
    protected DataSource reader;

    protected final Class<Entity> entityClass;
    protected final Class<ID> idClass;

    public BaseService() {
        //通过this(子类）得到当前类的信息（泛参类型）
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        //得到本类的参数类型
        var actualTypeArguments = type.getActualTypeArguments();
        //得到第一个参数还是泛参类型因此需要得到RawType
        this.entityClass = (Class<Entity>) ((ParameterizedType) actualTypeArguments[0]).getRawType();
        //得到第二个参数类型
        this.idClass = (Class<ID>) actualTypeArguments[1];
    }

    @Override
    public Optional<Entity> findById(ID id) {
        var entityInfo = PlaceHolder.genEntityInfo(this.entityClass);
        var table = Table.create(entityInfo.getName());
        var entity_id = table.bigIntColumn(entityInfo.getId().getName()).build();
        var select = Queries.select().where(entity_id.eq((Long) id));
        List<Entity> list = this.find(entity_id.eq((Integer) id));
        if (list.isEmpty()) {
            return Optional.of(null);
        }
        return Optional.of(list.get(0));
    }

    @Override
    public List<Entity> findAll() {
        var entityInfo = PlaceHolder.genEntityInfo(this.entityClass);
        var queryTable = Table.create(entityInfo.getName());
        var status = queryTable.intColumn("status").build();
        return this.find(status.eq(Status.OK.ordinal()));
    }

    @Override
    public List<Entity> findAllById(ID... ids) {
        var entityInfo = PlaceHolder.genEntityInfo(this.entityClass);
        var table = Table.create(entityInfo.getName());
        var entity_id = table.bigIntColumn(entityInfo.getId().getName()).build();
        var condition = Condition.emptyCondition();
        for (var id : ids) {
            condition = condition.or(entity_id.eq((Long) id));
        }
        return this.find(condition);
    }

    @Override
    public long count() {
        var entityInfo = PlaceHolder.genEntityInfo(this.entityClass);
        var table = Table.create(entityInfo.getName());
        var select = Queries.select(Function.count().as("cnt"))
                .from(table);
        return this.executeSql(select.build());
    }

    @Override
    public Page<Entity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public List<Entity> find(Condition where) {
        var entityInfo = PlaceHolder.genEntityInfo(this.entityClass);
        var table = Table.create(entityInfo.getName());

        var select = Queries.select().where(where);

        List<Entity> ls = new ArrayList<>();
        final Map<String, Method> methods = new HashMap<>();
        for (var method : this.entityClass.getMethods()) {
            if (!method.getName().startsWith("set")) {
                continue;
            }
            if (methods.containsKey(method.getName())) {
                continue;
            }
            Type[] paraType = method.getGenericParameterTypes();
            if (paraType.length != 1) {
                continue;
            }
            methods.put(method.getName(), method);
        }

        try (var conn = this.reader.getConnection();
             var pst = conn.prepareStatement(select.build());
             var rs = pst.executeQuery();) {
            //取得ResultSet的列名
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnsCount = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columnsCount];
            for (int i = 0; i < columnsCount; i++) {

                columnNames[i] = resultSetMetaData.getColumnLabel(i + 1);
            }
            while (rs.next()) {
                Entity bean = (Entity) this.entityClass.newInstance();
                //反射, 从ResultSet绑定到JavaBean
                for (int i = 0; i < columnNames.length; i++) {
                    //取得Set方法
                    String name = NamingUtils.underlineToCamel("set_" + columnNames[i]);
                    Method method = methods.get(name);
                    if (method == null) {
                        continue;
                    }
                    var paraType = Arrays.stream(method.getGenericParameterTypes()).findFirst().get().toString();
                    Object value = null;
                    if (paraType.indexOf("long") >= 0 || paraType.indexOf("bigint") >= 0)
                        value = rs.getLong(columnNames[i]);
                    else if (paraType.indexOf("Integer") >= 0)
                        value = rs.getInt(columnNames[i]);
                    else if (paraType.indexOf("String") >= 0)
                        value = rs.getString(columnNames[i]);
                    else if (paraType.indexOf("Double") >= 0)
                        value = rs.getDouble(columnNames[i]);
                    else if (paraType.indexOf("Float") >= 0)
                        value = rs.getFloat(columnNames[i]);
                    else if (paraType.indexOf("java.sql.Date") >= 0)
                        value = rs.getDate(columnNames[i]);
                    else if (paraType.indexOf("java.util.Date") >= 0)
                        value = new java.util.Date(rs.getTimestamp(columnNames[i]).getTime());
                    else if (paraType.indexOf("Time") >= 0)
                        value = rs.getDate(columnNames[i]);
                    else if (paraType.indexOf("Timestamp") >= 0)
                        value = rs.getTimestamp(columnNames[i]);
                    else if (paraType.indexOf("BigDecimal") >= 0)
                        value = rs.getBigDecimal(columnNames[i]);
                    else if (paraType.indexOf("boolean") >= 0)
                        value = rs.getBoolean(columnNames[i]);
                    else if (paraType.indexOf("Short") >= 0)
                        value = rs.getShort(columnNames[i]);
                    else if (paraType.indexOf("Byte") >= 0)
                        value = rs.getByte(columnNames[i]);
                    else if (paraType.contains(Status.class.getCanonicalName())) {
                        var status = rs.getInt(columnNames[i]);
                        for (var v : Status.values()) {
                            if (v.ordinal() == status) {
                                value = v;
                                break;
                            }
                        }
                    } else {
                        log.info("{}", paraType);
                        value = (Status) rs.getObject(columnNames[i]);
                    }
                    method.invoke(bean, value);
                }
                ls.add(bean);
            }
        } catch (Exception e) {
            log.error("getObjectList", e);
        }
        return ls;

    }

    @Override
    public Entity update(Entity entity) {
        return null;
    }

    @Override
    public void updateAll(Entity... entities) {

    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void deleteAll(Entity... entities) {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public Entity insert(Entity entity) {
        return null;
    }

    @Override
    public void insertAll(Entity... entities) {

    }

    @Override
    public int executeSql(String sql) {
        try (var conn = this.reader.getConnection();
             var pst = conn.prepareStatement(sql);
             var rs = pst.executeQuery();) {
            while (rs.next()) {
                return rs.getInt(0);
            }
        } catch (Exception e) {
            log.error("getObjectList", e);
        }
        return -1;
    }


}
