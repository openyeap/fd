package ltd.fdsa.database.sql.queries;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.columns.datetime.DateColumn;
import ltd.fdsa.database.sql.columns.datetime.DateTimeColumn;
import ltd.fdsa.database.sql.columns.datetime.TimeColumn;
import ltd.fdsa.database.sql.columns.number.NumberColumn;
import ltd.fdsa.database.sql.columns.string.StringColumn;
import ltd.fdsa.database.sql.dialect.Dialect;
import ltd.fdsa.database.sql.domain.*;
import ltd.fdsa.database.sql.functions.Function;
import ltd.fdsa.database.sql.queryexecutor.QueryExecutor;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.utils.Indentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static lombok.AccessLevel.PACKAGE;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@NoArgsConstructor(access = PACKAGE)
@Getter
@ToString
public class Insert implements UpdatebleQuery
{
    private Table table;
    private Map<Column, Valuable> values = new LinkedHashMap<>();

    Insert(Insert insert)
    {
        table = insert.table;
        values = new LinkedHashMap<>(insert.values);
    }

    public Insert into(Table insertTable)
    {
        table = insertTable;
        return this;
    }

    public Insert set(StringColumn<?> column, CharSequence value)
    {
        return addValue(column, new PlainValuable(value));
    }

    public Insert set(NumberColumn<?, ?> column, Number value)
    {
        return addValue(column, new PlainValuable(value));
    }

    public Insert set(NumberColumn<?, ?> column, long value)
    {
        return addValue(column, new PlainValuable(Long.valueOf(value)));
    }

    public Insert set(NumberColumn<?, ?> column, double value)
    {
        return addValue(column, new PlainValuable(Double.valueOf(value)));
    }

    public Insert set(Column column, Placeholder placeholder)
    {
        return addValue(column, placeholder);
    }

    public Insert set(Column column, Function function)
    {
        return addValue(column, new ValuableFunction(function));
    }

    public Insert set(Column column, Column otherColumn)
    {
        return addValue(column, new ValuableColumn(otherColumn));
    }

    public Insert set(DateColumn column, LocalDate value)
    {
        return addValue(column, new PlainValuable(value));
    }

    public Insert set(DateTimeColumn column, LocalDateTime value)
    {
        return addValue(column, new PlainValuable(value));
    }

    public Insert set(TimeColumn column, LocalTime value)
    {
        return addValue(column, new PlainValuable(value));
    }

    private Insert addValue(Column column, Valuable value)
    {
        values.put(column, value);
        return this;
    }

    @Override
    public String build(Dialect dialect, Indentation indentation)
    {
        return dialect.build(this, indentation);
    }

    public static Insert copy(Insert insert)
    {
        return new Insert(insert);
    }

    public long executeAndReturnKey(QueryExecutor queryExecutor)
    {
        return queryExecutor.executeAndReturnKey(this);
    }
}
