package ltd.fdsa.fql.util;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.database.sql.dialect.Dialects;
import ltd.fdsa.database.sql.queries.Select;
import ltd.fdsa.database.sql.schema.Schema;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.fql.antlr.FqlParser;
import ltd.fdsa.fql.model.ColumnInfo;
import ltd.fdsa.fql.model.ColumnSet;
import ltd.fdsa.fql.model.TableInfo;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

@Slf4j
public class FqlUtil {
    final DataSource writer;
    final Properties tableCode;
    final Map<String, Properties> columnCode;

    public FqlUtil(DataSource dataSource, Properties properties, Map<String, Properties> columnCode) {
        this.writer = dataSource;
        this.tableCode = properties;
        this.columnCode = columnCode;
        for (var table : listAllTables("", "")) {
            if (!this.tableCode.containsKey(table.getName())) {
                this.tableCode.setProperty(table.getName(), table.getName());
            }
            if (!this.columnCode.containsKey(table.getName())) {
                this.columnCode.put(table.getName(), new Properties());
            }
            var p = this.columnCode.get(table.getName());
            for (var c : table.getColumns()) {
                if (!p.containsKey(c.getName())) {
                    p.put(c.getName(), c.getName());
                }
            }
        }
    }

    List<Table> listAllTables(String catalog, String schemaPattern) {
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

    public ColumnSet getColumnSet(String table) {
        var columnSet = new ColumnSet();
        for (var entry : this.columnCode.get(table).entrySet()) {
            columnSet.put(entry.getKey().toString(), entry.getKey().toString(), entry.getValue().toString(), false);
        }
        return columnSet;
    }

    public TableInfo getTableInfo(FqlParser.SelectionContext ctx) {
        var alias = ctx.alias() == null ? "" : ctx.alias().name().getText();
        var name = ctx.name() == null ? "" : ctx.name().getText();
        if (Strings.isNullOrEmpty(alias)) {
            alias = name;
        }
        var code = this.tableCode.getProperty(name, name);
        return new TableInfo(name, alias, code);
    }

    public ColumnInfo getColumnInfo(FqlParser.SelectionContext ctx, String table) {
        var alias = ctx.alias() == null ? "" : ctx.alias().name().getText();
        var name = ctx.name() == null ? "" : ctx.name().getText();
        if ("...".equals(name)) {
            return new ColumnInfo("...", "...", "...", false);
        }
        if (Strings.isNullOrEmpty(alias)) {
            alias = name;
        }
        if (!this.columnCode.containsKey(table)) {
            this.columnCode.put(table, new Properties());
        }
        var code = this.columnCode.get(table).getProperty(name, name);
        return new ColumnInfo(name, alias, code, false);
    }

    public List<Map<String, Object>> fetchData(Select select) {
        var data = new ArrayList<Map<String, Object>>();
        var sql = select.build(Dialects.MYSQL);
        System.out.println(sql);
        try (var conn = this.writer.getConnection();
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
                var item = new TreeMap<String, Object>();
                for (int i = 0; i < columnNames.length; i++) {
                    item.put(columnNames[i], rs.getObject(columnNames[i]));
                }
                data.add(item);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
}
