package cn.zhumingwu.database.sql.columns.number.doubletype;

import lombok.ToString;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

import static java.sql.Types.DOUBLE;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class DoubleColumn extends DoubleTypeColumn<DoubleColumn> {
    public DoubleColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, DOUBLE);
    }
}
