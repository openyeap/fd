package ltd.fdsa.database.sql.columns.datetime;

import java.time.LocalDateTime;

import ltd.fdsa.database.sql.columns.ColumnBuilder;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;

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
