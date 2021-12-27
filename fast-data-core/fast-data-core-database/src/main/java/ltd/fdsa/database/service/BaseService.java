package ltd.fdsa.database.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.database.config.DataSourceConfig;
import ltd.fdsa.database.entity.BaseEntity;
import ltd.fdsa.database.entity.Status;
import ltd.fdsa.database.repository.IMetaData;
import ltd.fdsa.database.sql.conditions.Condition;
import ltd.fdsa.database.sql.functions.Function;
import ltd.fdsa.database.sql.queries.Queries;
import ltd.fdsa.database.sql.schema.Schema;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.utils.PlaceHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

@Slf4j
public class BaseService<Entity extends BaseEntity<ID>, ID> implements DataAccessService<Entity, ID>, IMetaData {

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

    @Override
    public List<Table> listAllTables(String catalog, String schemaPattern) {
        List<Table> result = new ArrayList<>();
        try (var conn = this.writer.getConnection()) {
            if (Strings.isNullOrEmpty(catalog)) {
                catalog = conn.getCatalog();
            }
            if (catalog == "*") {
                catalog = null;
            }
            if (Strings.isNullOrEmpty(schemaPattern)) {
                schemaPattern = conn.getSchema();
            }
            if (schemaPattern == "*") {
                schemaPattern = null;
            }
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            var connectionMetaData = conn.getMetaData();
            ResultSet tableSet = connectionMetaData.getTables(catalog, schemaPattern, null, new String[]{"TABLE", "VIEW"});
            while (tableSet.next()) {
                Table table = Schema
                        .create(tableSet.getString("TABLE_SCHEM"))
                        .table(tableSet.getString("TABLE_NAME"))
                        .type(tableSet.getString("TABLE_TYPE"))
                        .remark(tableSet.getString("REMARKS"));
                ResultSet columnSet = conn.getMetaData().getColumns(catalog, schemaPattern, table.getName(), null);
                var columnsMetaData = columnSet.getMetaData();
                while (columnSet.next()) {
                    String columnRemark = columnSet.getString("REMARKS");
                    var typeName = columnSet.getString("TYPE_NAME");
                    var columnName = columnSet.getString("COLUMN_NAME");
                    var columnSize = columnSet.getInt("COLUMN_SIZE");
                    table.column(columnName)
                            .size(columnSize)
                            .type(typeName)
                            .build()
                            .remark(columnRemark);
                }
                result.add(table);
            }
        } catch (Exception ex) {
            log.error("listAllTables", ex);
        }
        return result;
    }

    @Override
    public Map<String, Object> listAllForeignKey(String tableName, String schemaPattern, String catalog) {
        Map<String, Object> map = new HashMap<>();
        try (var conn = this.writer.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            //根据表名获得外键
            ResultSet importedKeys = metaData.getImportedKeys(catalog, schemaPattern, tableName);
            ResultSetMetaData resultSetMetaData = importedKeys.getMetaData();

            while (importedKeys.next()) {
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    map.put(resultSetMetaData.getColumnName(i), importedKeys.getString(i));
                }
            }
        } catch (Exception ex) {
            log.error("listAllForeignKey", ex);
        }
        return map;
    }
}
