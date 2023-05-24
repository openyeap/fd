package cn.zhumingwu.database.sql.columns.number.integer;

import lombok.ToString;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

import static java.sql.Types.TINYINT;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class TinyIntColumn extends IntegerColumn<TinyIntColumn> {
    public TinyIntColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, TINYINT);
    }
}
