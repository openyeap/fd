package ltd.fdsa.database.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.database.config.DataSourceConfig;
import ltd.fdsa.database.entity.BaseEntity;
import ltd.fdsa.database.entity.Status;
import ltd.fdsa.database.repository.DAO;
import ltd.fdsa.database.repository.IMetaData;
import ltd.fdsa.database.sql.queries.Queries;
import ltd.fdsa.database.sql.queries.Select;
import ltd.fdsa.database.sql.schema.Schema;
import ltd.fdsa.database.sql.schema.Table;
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
        var table = this.entityClass.getAnnotation(javax.persistence.Table.class);
        var entity_id = Table.create(table.name()).charColumn("id").build();
        var select = Queries.select().where(entity_id.eq(id.toString()));
        return DAO.getObjectList(this.reader, select.build().toString(), this.entityClass).stream().findFirst();
    }

    @Override
    public List<Entity> findAll() {
        var table = this.entityClass.getAnnotation(javax.persistence.Table.class);
        var queryTable = Table.create(table.name());
        var select = Queries.select().from(queryTable);
        return DAO.getObjectList(this.reader, select.build().toString(), this.entityClass);

    }

    @Override
    public List<Entity> findAllById(ID... ids) {
        List<Entity> result = new ArrayList<>();
        for (var id : ids) {
            var e = this.findById(id);
            if (e.isPresent()) {
                result.add(e.get());
            }
        }
        return result;
    }

    @Override
    public long count() {

        return this.findAll().size();
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
    public List<Entity> findWhere(Select select) {

        return DAO.getObjectList(this.reader, select.build(), this.entityClass);

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
        return 0;
    }

    protected <T> List<T> getDataFromSource(DataSource dataSource, String sql) {
        List<T> ls = new ArrayList<>();
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
        try (var conn = dataSource.getConnection();
             var pst = conn.prepareStatement(sql);
             var rs = pst.executeQuery();) {

            //取得ResultSet的列名
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnsCount = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columnsCount];
            for (int i = 0; i < columnsCount; i++) {

                columnNames[i] = resultSetMetaData.getColumnLabel(i + 1);
            }
            while (rs.next()) {
                T bean = (T) this.entityClass.newInstance();
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
                        .create(tableSet.getString("TABLE_CAT"))
                        .table(tableSet.getString("TABLE_NAME"))
                        .type(tableSet.getString("TABLE_TYPE"))
                        .description(tableSet.getString("REMARKS"));
                ResultSet columnSet = conn.getMetaData().getColumns(null, schemaPattern, table.getName(), null);
                var columnsMetaData = columnSet.getMetaData();
                while (columnSet.next()) {
                    var typeName = columnSet.getString("TYPE_NAME");
                    switch (typeName) {
                        case "BIGINT":
                            table.bigIntColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();
                            break;
                        case "DATETIME":
                            table.dateTimeColumn(columnSet.getString("COLUMN_NAME"))
                                    .build();
                            break;
                        case "VARCHAR":
                            table.varCharColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();

                            break;
                        case "INT":
                            table.intColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();
                            break;
                        case "TINYINT":
                            table.tinyIntColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();

                            break;
                        case "DATE":
                            table.dateColumn(columnSet.getString("COLUMN_NAME"))
                                    .build();

                            break;
                        case "DOUBLE":
                            table.doubleColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();
                            break;
                        case "TIMESTAMP":
                            table.floatColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();
                            break;
                        case "TINYBLOB":
                            table.column(columnSet.getString("COLUMN_NAME"))
                                    .type(typeName)
                                    .build();
                            break;
                        case "TEXT":
                            table.textColumn(columnSet.getString("COLUMN_NAME"))
                                    .build();
                            break;
                        case "BIT":
                            table.column(columnSet.getString("COLUMN_NAME"))
                                    .type(typeName)
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();
                            break;
                        case "CHAR":
                            table.charColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();
                            break;
                        case "SMALLINT":
                            table.smallIntColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();
                            break;

                        default:
                            log.warn("没有考虑到的类型：{}", typeName);
                            table.varCharColumn(columnSet.getString("COLUMN_NAME"))
                                    .size(Integer.parseInt(columnSet.getString("COLUMN_SIZE")))
                                    .build();
                    }
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
