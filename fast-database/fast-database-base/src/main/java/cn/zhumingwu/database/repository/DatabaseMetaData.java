package cn.zhumingwu.database.repository;


import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.schema.Table;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface DatabaseMetaData {
    List<Table> listAllTables(String catalog, String schemaPattern);
    Map<String, Object> listAllForeignKey(String tableName, String schemaPattern, String catalog);
    default List<Column> listAllFields(String tableName, String schemaPattern, String catalog) {
        for (var table : listAllTables(catalog, schemaPattern)) {
            if (table.getName().contains(tableName)) {
                return Arrays.asList(table.getColumns());
            }
        }
        return Collections.emptyList();
    }
}
