package cn.zhumingwu.database.sql.columns.number.integer;

import lombok.ToString;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

import static java.sql.Types.BIGINT;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class BigIntColumn extends IntegerColumn<BigIntColumn> {
    public BigIntColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, BIGINT);
    }
}
