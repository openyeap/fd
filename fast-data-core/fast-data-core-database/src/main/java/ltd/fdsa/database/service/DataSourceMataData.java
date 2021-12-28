package ltd.fdsa.database.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.database.repository.DatabaseMetaData;
import ltd.fdsa.database.sql.schema.Schema;
import ltd.fdsa.database.sql.schema.Table;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class DataSourceMataData implements DatabaseMetaData {

    final DataSource dataSource;

    public DataSourceMataData(DataSource writer) {
        this.dataSource = writer;
    }

    @Override
    public List<Table> listAllTables(String catalog, String schema) {
        List<Table> result = new LinkedList<>();
        try (var conn = this.dataSource.getConnection()) {
            if (Strings.isNullOrEmpty(catalog)) {
                catalog = conn.getCatalog();
            }
            if (catalog == "*") {
                catalog = null;
            }
            if (Strings.isNullOrEmpty(schema)) {
                schema = conn.getSchema();
            }
            if (schema == "*") {
                schema = null;
            }
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            var metaData = conn.getMetaData();
            ResultSet tableSet = metaData.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
            while (tableSet.next()) {
                Table table = Schema
                        .create(tableSet.getString("TABLE_SCHEM"))
                        .table(tableSet.getString("TABLE_NAME"))
                        .type(tableSet.getString("TABLE_TYPE"))
                        .remark(tableSet.getString("REMARKS"));
                ResultSet columnSet = metaData.getColumns(catalog, schema, table.getName(), null);
                var columnsMetaData = columnSet.getMetaData();
                while (columnSet.next()) {
                    var columnRemark = columnSet.getString("REMARKS");
                    var columnType = columnSet.getString("TYPE_NAME");
                    var columnName = columnSet.getString("COLUMN_NAME");
                    var columnSize = columnSet.getInt("COLUMN_SIZE");
                    table.column(columnName)
                            .size(columnSize)
                            .type(columnType)
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
        try (var conn = this.dataSource.getConnection()) {
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            var metaData = conn.getMetaData();
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
