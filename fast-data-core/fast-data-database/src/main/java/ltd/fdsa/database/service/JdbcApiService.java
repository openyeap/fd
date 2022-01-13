package ltd.fdsa.database.service;

import com.google.common.base.Strings;
import io.swagger.models.*;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.properties.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.database.model.RowDataMapper;
import ltd.fdsa.database.properties.JdbcApiProperties;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.dialect.Dialect;
import ltd.fdsa.database.sql.queries.Query;
import ltd.fdsa.database.sql.queries.Select;
import ltd.fdsa.database.sql.schema.Table;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.AntPathMatcher;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JdbcApiService {

    final JdbcApiProperties properties;
    final DataSource dataSource;
    @Getter
    final Map<String, Table> namedTables;
    final Map<String, JdbcApiProperties.Acl[]> namedAcl;
    @Getter
    final Map<String, Map<String, Column>> namedColumns;
    @Getter
    final Map<String, List<String>> namedKeyColumns;

    final Dialect dialect;

    public JdbcApiService(JdbcApiProperties properties, DataSource dataSource) {
        this.dataSource = dataSource;
        this.properties = properties;
        this.namedTables = new HashMap<>();
        this.namedAcl = new HashMap<>();

        var mataData = new DataSourceMataData(this.dataSource);

        this.dialect = Dialect.forName(this.getDatabaseProductName());

        var tables = mataData.listAllTables(this.properties.getCatalog(), this.properties.getSchema());
        this.namedKeyColumns = new HashMap<>();
        rename(tables, mataData);

        this.namedTables.putAll(tables.stream().collect(Collectors.toMap(k -> k.getAlias(), v -> v)));

        this.namedColumns = new HashMap<>();
        for (var entry : this.namedTables.entrySet()) {
            if (!this.namedColumns.containsKey(entry.getKey())) {
                this.namedColumns.put(entry.getKey(), new HashMap<>());
            }
            var table = Arrays.stream(entry.getValue().getColumns())
                    .collect(Collectors.toMap(k -> k.getAlias(), c -> c));
            this.namedColumns.get(entry.getKey()).putAll(table);
        }
    }

    String getDatabaseProductName() {
        try {
            return this.dataSource.getConnection().getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            return "null";
        }
    }

    public List<Map<String, Object>> query(Select select) {
        var data = new ArrayList<Map<String, Object>>();
        var sql = select.build(this.dialect);
        log.debug("sql: {}", sql);
        try (var conn = this.dataSource.getConnection();
             var pst = conn.prepareStatement(sql);
             var rs = pst.executeQuery()) {
            // 取得ResultSet的列名
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnsCount = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columnsCount];
            for (int i = 0; i < columnsCount; i++) {
                columnNames[i] = resultSetMetaData.getColumnLabel(i + 1);
            }
            while (rs.next()) {
                var item = new TreeMap<String, Object>();
                for (int i = 0; i < columnNames.length; i++) {
                    item.put(columnNames[i], rs.getObject(columnNames[i]));
                }
                data.add(item);
            }
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return data;
    }

    public List<Map<String, Object>> query(Query query, Map<String, ?> args) {
        var jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
        var sql = query.build(this.dialect);
        log.debug("sql: {}", sql);
        return jdbcTemplate.query(sql, args, new RowDataMapper());
    }

    public int update(Query query, Map<String, ?> args) {
        var jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
        var sql = query.build(this.dialect);
        log.debug("query: {}", sql);
        return jdbcTemplate.update(sql, args);
    }

    public Swagger getApiDocs(String host, String basePath) {

        Swagger swagger = new Swagger();
        {
            Info info = new Info();
            info.title(this.properties.getTitle());
            info.description(this.properties.getDescription());
            Contact contact = new Contact();
            contact.email(this.properties.getEmail());
            contact.name(this.properties.getName());
            contact.url(this.properties.getUrl());
            info.contact(contact);
            swagger.info(info);
        }
        swagger.host(host);
        swagger.basePath(basePath);
        for (var table : this.namedTables.values()) {
            // api 接口列表
            var tag = new Tag();
            tag.name(table.getAlias());
            tag.description(table.getRemark());
            swagger.tag(tag);

            ModelImpl model = new ModelImpl();
            for (var column : table.getColumns()) {
                switch (column.getColumnDefinition().getDefinitionName().toUpperCase(Locale.ROOT)) {
                    case "DATE":
                        model.property(column.getAlias(), new DateProperty().description(column.getRemark()));
                        break;
                    case "TIME":
                    case "TIMESTAMP":
                    case "DATETIME":
                        model.property(column.getAlias(), new DateTimeProperty().description(column.getRemark()));
                        break;
                    case "CHAR":
                    case "TEXT":
                    case "VARCHAR":
                    case "NCHAR":
                    case "NVARCHAR":
                    case "BPCHAR":
                    case "LONGTEXT":
                        model.property(column.getAlias(), new StringProperty().description(column.getRemark()));
                        break;
                    case "BOOLEAN":
                    case "BOOL":
                        model.property(column.getAlias(), new BooleanProperty().description(column.getRemark()));
                        break;
                    case "BIGINT":
                    case "LONG":
                    case "INT8":
                    case "BIGSERIAL":
                        model.property(column.getAlias(), new LongProperty().description(column.getRemark()));
                        break;
                    case "SMALLINT":
                    case "TINYINT":
                    case "INTEGER":
                    case "INT":
                    case "INT2":
                    case "INT4":
                        model.property(column.getAlias(), new IntegerProperty().description(column.getRemark()));
                        break;
                    case "FLOAT":
                        model.property(column.getAlias(), new FloatProperty().description(column.getRemark()));
                        break;
                    case "DOUBLE":
                    case "FLOAT8":
                        model.property(column.getAlias(), new DoubleProperty().description(column.getRemark()));
                        break;
                    case "NUMERIC":
                    case "DECIMAL":
                        model.property(column.getAlias(), new DecimalProperty().description(column.getRemark()));
                        break;
                    default:
                        log.warn("没有考虑到的类型：{}", column.getColumnDefinition().getDefinitionName());
                        model.property(column.getAlias() + ":" + column.getColumnDefinition().getDefinitionName(),
                                new StringProperty().description(column.getRemark()));
                        break;

                }
            }
            swagger.addDefinition(table.getAlias(), model);

            // api 路径
            Path commonPath = new Path();
            Path useKeyPath = new Path();
            for (var acl : this.namedAcl.get(table.getAlias())) {
                switch (acl) {
                    case CREATE:
                        commonPath.put(createOperation(table, model));
                        break;
                    case QUERY_BY_KEY:
                        useKeyPath.get(queryByKeyOperation(table, model));
                        break;
                    case DELETE:
                        commonPath.delete(deleteOperation(table, model));
                        break;
                    case DELETE_BY_KEY:
                        useKeyPath.delete(deleteByKeyOperation(table, model));
                        break;
                    case UPDATE:
                        commonPath.post(updateOperation(table, model));
                        break;
                    case UPDATE_BY_KEY:
                        useKeyPath.post(updateByKeyOperation(table, model));
                        break;
                    default:
                        break;
                }
            }
            swagger.path("/v2/" + table.getAlias(), commonPath);
            swagger.path("/v2/" + table.getAlias() + "/{key}", useKeyPath);
        }

        var path = new Path();
        path.post(queryOperation());
        swagger.path("/v2/query", path);

        return swagger;
    }

    private Operation createOperation(Table table, Model model) {
        Operation operation = new Operation();
        operation.tag(table.getAlias());
        operation.summary("Create data for " + table.getAlias());
        operation.description(table.getRemark());
        operation.operationId(JdbcApiProperties.Acl.CREATE.name());
        operation.consumes("application/json");
        operation.produces("*/*");
        // parameter
        var parameter = new BodyParameter();
        parameter.name("data");
        parameter.description("data (k-v pairs):\n\n" +
                "{\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;\"key1\":\"value1\",\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;\"key2\":\"value2\" \n" +
                "}");
        parameter.schema(model);
        parameter.setRequired(true);
        operation.parameter(parameter);

        // response
        Response response = new Response();
        response.description("OK");
        var responseSchema = new ModelImpl();
        responseSchema.description("");
        responseSchema.name("data");
        responseSchema.property("data", new BooleanProperty().description("data"));
        responseSchema.property("code", new IntegerProperty().description("code"));
        response.setResponseSchema(responseSchema);
        return operation.response(200, response);
    }

    private Operation queryOperation() {
        Operation operation = new Operation();
        operation.tag("Multi Resources Query");
        operation.summary("Query multi-resources by fast query language");
        operation.description("Fast query language is one member of GraphQL family");
        operation.operationId("query");
        operation.consumes("application/fql");
        operation.produces("*/*");

        // parameter
        operation.parameter(new BodyParameter()
                .name("query")
                .description("Fast query language：\n\n" +
                        "{\n" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;user : t_user(user_id_eq:1) {\n" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;name\n" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;user_id\n" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;roles : t_user_role(user_id_eq:$user_id) {\n" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;role_id\n" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;role_name : t_role(role_id_eq: $role_id) {\n"
                        +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;name\n"
                        +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                        "}")

        );
        // response
        Response response = new Response();
        response.description("OK");
        var responseSchema = new ModelImpl();
        responseSchema.description("");
        responseSchema.name("data");
        responseSchema.property("data", new ArrayProperty().description("list").items(new MapProperty()));
        responseSchema.property("code", new IntegerProperty().description("code"));
        response.setResponseSchema(responseSchema);
        return operation.response(200, response);
    }

    private Operation deleteByKeyOperation(Table table, ModelImpl model) {
        Operation operation = new Operation();
        operation.tag(table.getAlias());
        operation.summary("Delete data from " + table.getAlias() + " by unique keys");
        operation.description(table.getRemark());
        operation.operationId(JdbcApiProperties.Acl.DELETE.name());
        operation.consumes("application/json");
        operation.produces("*/*");
        // parameter
        operation.parameter(new PathParameter().name("key").description("unique keys").required(true));
        // response
        Response response = new Response();
        response.description("OK");
        var responseSchema = new ModelImpl();
        responseSchema.description("");
        responseSchema.name("data");
        responseSchema.property("data", new BooleanProperty().description("data"));
        responseSchema.property("code", new IntegerProperty().description("code"));
        response.setResponseSchema(responseSchema);
        return operation.response(200, response);
    }

    private Operation deleteOperation(Table table, ModelImpl model) {
        Operation operation = new Operation();
        operation.tag(table.getAlias());
        operation.summary("Delete data from" + table.getAlias());
        operation.description(table.getRemark());
        operation.operationId(JdbcApiProperties.Acl.DELETE.name());
        operation.consumes("application/json");
        operation.produces("*/*");
        // parameter
        var parameter = new BodyParameter();
        parameter.name("where");
        parameter.description("where (k-v pairs):\n\n" +
                "{\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;\"key1\":\"value1\",\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;\"key2\":\"value2\" \n" +
                "}");
        parameter.schema(model);
        parameter.setRequired(true);
        operation.parameter(parameter);
        // response
        Response response = new Response();
        response.description("OK");
        var responseSchema = new ModelImpl();
        responseSchema.description("");
        responseSchema.name("data");
        responseSchema.property("data", new BooleanProperty().description("data"));
        responseSchema.property("code", new IntegerProperty().description("code"));
        response.setResponseSchema(responseSchema);
        return operation.response(200, response);
    }

    private Operation updateByKeyOperation(Table table, ModelImpl model) {
        Operation operation = new Operation();
        operation.tag(table.getAlias());
        operation.summary("Update " + table.getAlias() + " using json object by by unique keys");
        operation.description(table.getRemark());
        operation.operationId(JdbcApiProperties.Acl.UPDATE.name());
        operation.consumes("application/json");
        operation.produces("*/*");
        // parameter
        operation.parameter(new PathParameter().name("key").description("unique keys").required(true));
        var parameter = new BodyParameter();
        parameter.name("data");
        parameter.description("data (k-v pairs):\n" +
                "{\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;\"key1\":\"value1\",\n" +
                "&nbsp;&nbsp;&nbsp;&nbsp;\"key2\":\"value2\" \n" +
                "}");
        parameter.schema(model);
        parameter.setRequired(true);
        operation.parameter(parameter);
        // response
        Response response = new Response();
        response.description("OK");
        var responseSchema = new ModelImpl();
        responseSchema.description("");
        responseSchema.name("data");
        responseSchema.property("data", new BooleanProperty().description("data"));
        responseSchema.property("code", new IntegerProperty().description("code"));
        response.setResponseSchema(responseSchema);
        return operation.response(200, response);
    }

    private Operation updateOperation(Table table, ModelImpl model) {
        Operation operation = new Operation();
        operation.tag(table.getAlias());
        operation.summary("Update " + table.getAlias() + " using json object by custom conditions");
        operation.description(table.getRemark());
        operation.operationId(JdbcApiProperties.Acl.UPDATE.name());
        operation.consumes("application/json");
        operation.produces("*/*");
        // parameter
        var parameter = new BodyParameter();
        parameter.name("update");
        parameter.description("Update data and custom conditions");
        ModelImpl updateModel = new ModelImpl();
        ObjectProperty data = new ObjectProperty(model.getProperties());
        updateModel.property("data", data);

        ObjectProperty where = new ObjectProperty();
        for (var entry : model.getProperties().entrySet()) {
            if (entry.getKey().contains("key") || entry.getKey().contains("id")) {
                where.property(entry.getKey(), entry.getValue());
            }
        }
        if (where.getProperties().size() == 0) {
            where.property("id", new IntegerProperty());
        }
        updateModel.property("where", where);
        parameter.schema(updateModel);
        parameter.setRequired(true);
        operation.parameter(parameter);
        // response
        Response response = new Response();
        response.description("OK");
        var responseSchema = new ModelImpl();
        responseSchema.description("");
        responseSchema.name("data");
        responseSchema.property("data", new BooleanProperty().description("data"));
        responseSchema.property("code", new IntegerProperty().description("code"));
        response.setResponseSchema(responseSchema);
        return operation.response(200, response);
    }

    private Operation queryByKeyOperation(Table table, Model model) {
        Operation operation = new Operation();
        operation.tag(table.getAlias());
        operation.summary("Query one data from " + table.getAlias() + " by unique keys");
        operation.description(table.getRemark());
        operation.operationId(JdbcApiProperties.Acl.QUERY_BY_KEY.name());
        operation.consumes("application/json");
        operation.produces("*/*");
        // parameter
        operation.parameter(new PathParameter().name("key").description("unique keys").required(true));

        // response
        Response response = new Response();
        response.description("OK");
        var responseSchema = new ModelImpl();
        responseSchema.description("");
        responseSchema.name("data");
        responseSchema.property("data", new ObjectProperty(model.getProperties()));
        responseSchema.property("code", new IntegerProperty().description("code"));
        response.setResponseSchema(responseSchema);
        return operation.response(200, response);
    }

    private List<Table> rename(List<Table> list, DataSourceMataData mataData) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (var table : list) {
            for (var entry : this.properties.getTables().entrySet()) {
                if (pathMatcher.match(entry.getKey(), table.getName())) {
                    var tableName = table.getName();
                    var newTable = rename(tableName, entry.getValue());
                    table.as(newTable);
                    for (var column : table.getColumns()) {
                        var columnName = column.getName();
                        var newColumn = rename(columnName, entry.getValue().getColumn());
                        column.as(newColumn);
                    }
                }
            }

            if (Strings.isNullOrEmpty(table.getAlias())) {
                table.as(table.getName());
            }
            var keys = mataData.listAllForeignKey(table.getName(), this.properties.getSchema(),
                    this.properties.getCatalog());
            for (var column : table.getColumns()) {
                if (Strings.isNullOrEmpty(column.getAlias())) {
                    column.as(column.getName());
                }
                if (keys.containsKey(column.getName())) {
                    if (!this.namedKeyColumns.containsKey(table.getAlias())) {
                        this.namedKeyColumns.put(table.getAlias(), new LinkedList<String>());
                    }
                    this.namedKeyColumns.get(table.getAlias()).add(column.getAlias());
                }
            }
            for (var entry : this.properties.getAcl().entrySet()) {
                if (pathMatcher.match(entry.getKey(), table.getName())) {
                    this.namedAcl.put(table.getAlias(), entry.getValue());
                }
            }
            if (!this.namedAcl.containsKey(table.getAlias())) {
                this.namedAcl.put(table.getAlias(), JdbcApiProperties.Acl.values());
            }
        }
        return list;
    }

    private String rename(String name, JdbcApiProperties.TableNameRule rule) {
        var newName = name;
        for (var i : rule.getRemoves()) {
            if (newName.startsWith(i.getPrefix())) {
                newName = newName.substring(i.getPrefix().length());
            }
            if (newName.endsWith(i.getSuffix())) {
                newName = newName.substring(0, newName.length() - i.getSuffix().length());
            }
        }
        for (var i : rule.getAppends()) {
            newName = i.getPrefix() + newName + i.getSuffix();
        }
        for (var i : rule.getReplaces().entrySet()) {
            newName = newName.replace(i.getKey(), i.getValue());
        }
        return newName;
    }

    private String rename(String name, JdbcApiProperties.ColumnNameRule rule) {
        var newName = name;
        for (var i : rule.getRemoves()) {
            if (newName.startsWith(i.getPrefix())) {
                newName = newName.substring(i.getPrefix().length());
            }
            if (newName.endsWith(i.getSuffix())) {
                newName = newName.substring(0, newName.length() - i.getSuffix().length());
            }
        }
        for (var i : rule.getAppends()) {
            newName = i.getPrefix() + newName + i.getSuffix();
        }

        for (var i : rule.getReplaces().entrySet()) {
            newName = newName.replace(i.getKey(), i.getValue());
        }
        return newName;
    }
}
