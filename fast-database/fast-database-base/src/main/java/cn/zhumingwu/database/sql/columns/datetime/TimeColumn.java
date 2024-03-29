package cn.zhumingwu.database.sql.columns.datetime;

import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.conditions.GenericCondition;
import lombok.ToString;
import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.domain.Placeholder;
import cn.zhumingwu.database.sql.schema.Table;

import java.time.LocalTime;

import static java.sql.Types.DATE;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class TimeColumn extends Column
{
    public TimeColumn(Table table, String name, String alias, ColumnDefinition columnDefinition)
    {
        super(table, name, alias, columnDefinition, DATE);
    }
    public Condition isEqualTo(LocalTime value)
    {
        return value == null ? new GenericCondition(GenericCondition.GenericConditionType.IS_NULL, this) : new GenericCondition(GenericCondition.GenericConditionType.IS_EQUAL_TO, this, value);
    }

    public Condition eq(LocalTime value)
    {
        return isEqualTo(value);
    }

    public Condition isNotEqualTo(LocalTime value)
    {
        return value == null ? new GenericCondition(GenericCondition.GenericConditionType.IS_NOT_NULL, this) : new GenericCondition(GenericCondition.GenericConditionType.IS_NOT_EQUAL_TO, this, value);
    }

    public Condition nEq(LocalTime value)
    {
        return isNotEqualTo(value);
    }

    public Condition isAfter(LocalTime value)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_GREATER_THAN, this, value);
    }

    public Condition isAfter(Column otherColumn)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_GREATER_THAN, this, otherColumn);
    }

    public Condition isAfter(Placeholder placeholder)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_GREATER_THAN, this, placeholder);
    }

    public Condition isAfterOrEqualTo(LocalTime value)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_GREATER_THAN_OR_EQUAL_TO, this, value);
    }

    public Condition isAfterOrEqualTo(Column otherColumn)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_GREATER_THAN_OR_EQUAL_TO, this, otherColumn);
    }

    public Condition isAfterOrEqualTo(Placeholder placeholder)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_GREATER_THAN_OR_EQUAL_TO, this, placeholder);
    }

    public Condition isBefore(LocalTime value)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_LESS_THAN, this, value);
    }

    public Condition isBefore(Column otherColumn)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_LESS_THAN, this, otherColumn);
    }

    public Condition isBefore(Placeholder placeholder)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_LESS_THAN, this, placeholder);
    }

    public Condition isBeforeOrEqualTo(LocalTime value)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_LESS_THAN_OR_EQUAL_TO, this, value);
    }

    public Condition isBeforeOrEqualTo(Column otherColumn)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_LESS_THAN_OR_EQUAL_TO, this, otherColumn);
    }

    public Condition isBeforeOrEqualTo(Placeholder placeholder)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_LESS_THAN_OR_EQUAL_TO, this, placeholder);
    }

    public Condition isBetween(LocalTime value1, LocalTime value2)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_BETWEEN, this, value1, value2);
    }

    public Condition isBetween(Column otherColumn1, Column otherColumn2)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_BETWEEN, this, otherColumn1, otherColumn2);
    }

    public Condition isBetween(LocalTime value, Column otherColumn)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_BETWEEN, this, value, otherColumn);
    }

    public Condition isBetween(Column otherColumn, LocalTime value)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_BETWEEN, this, otherColumn, value);
    }

    public Condition isBetween(Column otherColumn, Placeholder placeholder)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_BETWEEN, this, otherColumn, placeholder);
    }

    public Condition isBetween(LocalTime value, Placeholder placeholder)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_BETWEEN, this, value, placeholder);
    }

    public Condition isBetween(Placeholder placeholder, LocalTime value)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_BETWEEN, this, placeholder, value);
    }

    public Condition isBetween(Placeholder placeholder, Column otherColumn)
    {
        return new GenericCondition(GenericCondition.GenericConditionType.IS_BETWEEN, this, placeholder, otherColumn);
    }
}
