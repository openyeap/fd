package cn.zhumingwu.database.sql.columns.datetime;

import cn.zhumingwu.database.sql.columns.ColumnBuilder;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

import java.time.LocalDateTime;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class DateTimeColumnBuilder extends ColumnBuilder<DateTimeColumn, DateTimeColumnBuilder, LocalDateTime>
{
    public DateTimeColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected DateTimeColumn getColumnInstance()
    {
        return new DateTimeColumn(table, name, null, new ColumnDefinition("DATETIME", null, isNullable, isDefaultNull, false, false, defaultValue));
    }
}
