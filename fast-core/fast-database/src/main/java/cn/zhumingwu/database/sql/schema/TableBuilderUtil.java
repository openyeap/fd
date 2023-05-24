package cn.zhumingwu.database.sql.schema;

import cn.zhumingwu.database.sql.columns.Column;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@NoArgsConstructor(access = PRIVATE)
public final class TableBuilderUtil {
    public static void addColumnToTable(Column column, Table table) {
        table.addColumn(column);
    }
}
