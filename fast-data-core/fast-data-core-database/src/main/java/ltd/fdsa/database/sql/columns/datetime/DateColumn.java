package ltd.fdsa.database.sql.columns.datetime;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.conditions.DateTimeConditions;
import ltd.fdsa.database.sql.schema.Table;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.function.Function;

import static java.sql.Types.DATE;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class DateColumn extends Column implements DateTimeConditions {
    @Override
    public Function<ZonedDateTime, Temporal> getDateConversion() {
        return ZonedDateTime::toLocalDate;
    }

    public DateColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, DATE);
    }
}
