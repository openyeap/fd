package cn.zhumingwu.database.sql.columns.number.integer;

import lombok.ToString;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

import static java.sql.Types.INTEGER;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class IntColumn extends IntegerColumn<IntColumn> {
    public IntColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, INTEGER);
    }
}
