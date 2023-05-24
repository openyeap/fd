package cn.zhumingwu.database.sql.queries;

import cn.zhumingwu.database.sql.conditions.CombinedCondition;
import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.domain.*;
import cn.zhumingwu.database.sql.functions.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.columns.datetime.DateColumn;
import cn.zhumingwu.database.sql.columns.datetime.DateTimeColumn;
import cn.zhumingwu.database.sql.columns.number.NumberColumn;
import cn.zhumingwu.database.sql.columns.string.StringColumn;
import cn.zhumingwu.database.sql.dialect.Dialect;
import cn.zhumingwu.database.sql.domain.*;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.sql.utils.Indentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static lombok.AccessLevel.PACKAGE;
import static cn.zhumingwu.database.sql.domain.ConditionType.WHERE;
import static cn.zhumingwu.database.sql.domain.ConditionType.WHERE_NOT;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@RequiredArgsConstructor(access = PACKAGE)
@Getter
@ToString
public class Update implements UpdatebleQuery
{
    private final Table table;
    private Map<Column, Valuable> values = new LinkedHashMap<>();
    private Condition where;
    private ConditionType whereConditionType;

    Update(Update update)
    {
        table = update.table;
        values = new LinkedHashMap<>(update.values);
        where = CombinedCondition.getCopy(update.where);
        whereConditionType = update.whereConditionType;
    }

    public Update set(StringColumn<?> column, CharSequence value)
    {
        return set(column, new PlainValuable(value));
    }

    public Update set(NumberColumn<?, ?> column, Number value)
    {
        return set(column, new PlainValuable(value));
    }

    public Update set(NumberColumn<?, ?> column, long value)
    {
        return set(column, new PlainValuable(Long.valueOf(value)));
    }

    public Update set(NumberColumn<?, ?> column, double value)
    {
        return set(column, new PlainValuable(Double.valueOf(value)));
    }

    public Update set(Column column, Function function)
    {
        return set(column, new ValuableFunction(function));
    }

    public Update set(Column column, Column otherColumn)
    {
        return set(column, new ValuableColumn(otherColumn));
    }

    public Update set(DateColumn column, LocalDate value)
    {
        return set(column, new PlainValuable(value));
    }

    public Update set(DateTimeColumn column, LocalDateTime value)
    {
        return set(column, new PlainValuable(value));
    }

    public Update set(Column column, Valuable valuable)
    {
        values.put(column, valuable);
        return this;
    }

    public Update where(Condition condition)
    {
        where = condition;
        whereConditionType = WHERE;
        return this;
    }

    public Update whereNot(Condition condition)
    {
        where = condition;
        whereConditionType = WHERE_NOT;
        return this;
    }

    @Override
    public String build(Dialect dialect, Indentation indentation)
    {
        return dialect.build(this, indentation);
    }

    public static void clearWheres(Update update)
    {
        update.where = null;
    }

    public static Update copy(Update update)
    {
        return new Update(update);
    }
}
