package ltd.fdsa.database.repository;

import lombok.var;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.schema.Table;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface IMetaData {
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
