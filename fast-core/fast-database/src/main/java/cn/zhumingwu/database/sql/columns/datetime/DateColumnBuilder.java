package cn.zhumingwu.database.sql.columns.datetime;

import cn.zhumingwu.database.sql.columns.ColumnBuilder;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

import java.time.LocalDate;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class DateColumnBuilder extends ColumnBuilder<DateColumn, DateColumnBuilder, LocalDate>
{
    public DateColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected DateColumn getColumnInstance()
    {
        return new DateColumn(table, name, null, new ColumnDefinition("DATE", null, isNullable, isDefaultNull, false, false, defaultValue));
    }
}
