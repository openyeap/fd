package cn.zhumingwu.database.service;

import cn.zhumingwu.database.repository.DatabaseMetaData;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.database.sql.schema.Schema;
import cn.zhumingwu.database.sql.schema.Table;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
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
            ResultSet tableSet = metaData.getTables(catalog, schema, null, new String[] { "TABLE", "VIEW" });
            while (tableSet.next()) {
                Table table = Schema
                        .create(tableSet.getString("TABLE_SCHEM"))
                        .table(tableSet.getString("TABLE_NAME"))
                        .type(tableSet.getString("TABLE_TYPE"))
                        .remark(tableSet.getString("REMARKS"));
                ResultSet columnSet = metaData.getColumns(catalog, schema, table.getName(), null);
               
                while (columnSet.next()) {
                    var columnRemark = columnSet.getString("REMARKS");
                    var columnType = columnSet.getString("TYPE_NAME");
                    var columnName = columnSet.getString("COLUMN_NAME");
                    var columnSize = columnSet.getInt("COLUMN_SIZE");
                    var nullable = columnSet.getBoolean("NULLABLE");
                    // var IS_NULLABLE = columnSet.getBoolean("IS_NULLABLE");
                    // var IS_GENERATEDCOLUMN = columnSet.getBoolean("IS_GENERATEDCOLUMN");
                    // var IS_AUTOINCREMENT = columnSet.getBoolean("IS_AUTOINCREMENT");

                    table.column(columnName).nullable(nullable)
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
    public Map<String, Object> listAllForeignKey(String table, String schema, String catalog) {
        Map<String, Object> map = new LinkedHashMap<>();

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
            // 根据表名获得外键
            ResultSet importedKeys = metaData.getPrimaryKeys(catalog, schema, table);
            while (importedKeys.next()) {
                map.put(importedKeys.getString("COLUMN_NAME"), importedKeys.getString("PK_NAME"));
            }
        } catch (Exception ex) {
            log.error("listAllForeignKey", ex);
        }
        return map;
    }

}
