package cn.zhumingwu.database.sql.columns.number;

import cn.zhumingwu.database.sql.conditions.NumberConditions;
import lombok.ToString;
import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public abstract class NumberColumn<T extends NumberColumn<T, N>, N extends Number> extends Column implements NumberConditions {
    public NumberColumn(Table table, String name, String alias, ColumnDefinition columnDefinition, int sqlType) {
        super(table, name, alias, columnDefinition, sqlType);
    }
}
