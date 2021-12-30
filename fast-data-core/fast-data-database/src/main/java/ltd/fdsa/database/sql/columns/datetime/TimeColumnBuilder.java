package ltd.fdsa.database.sql.columns.datetime;

import java.time.LocalDate;

import ltd.fdsa.database.sql.columns.ColumnBuilder;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class TimeColumnBuilder extends ColumnBuilder<TimeColumn, TimeColumnBuilder, LocalDate>
{
    public TimeColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected TimeColumn getColumnInstance()
    {
        return new TimeColumn(table, name, null, new ColumnDefinition("TIME", null, isNullable, isDefaultNull, false, false, defaultValue));
    }
}
