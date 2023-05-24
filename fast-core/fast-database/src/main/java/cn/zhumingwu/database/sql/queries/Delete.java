package cn.zhumingwu.database.sql.queries;

import cn.zhumingwu.database.sql.conditions.CombinedCondition;
import cn.zhumingwu.database.sql.conditions.Condition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import cn.zhumingwu.database.sql.dialect.Dialect;
import cn.zhumingwu.database.sql.domain.ConditionType;
import cn.zhumingwu.database.sql.domain.Limit;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.sql.utils.Indentation;

import static lombok.AccessLevel.PACKAGE;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@NoArgsConstructor(access = PACKAGE)
@Getter
@ToString
public class Delete implements UpdatebleQuery
{
    private Table table;
    private Condition where;
    private ConditionType whereConditionType;
    private Limit limitation;

    Delete(Delete delete)
    {
        table = delete.table;
        where = CombinedCondition.getCopy(delete.where);
        whereConditionType = delete.whereConditionType;
        limitation = delete.limitation;
    }

    public Delete from(Table deleteTable)
    {
        table = deleteTable;
        return this;
    }

    public Delete where(Condition condition)
    {
        where = condition;
        whereConditionType = ConditionType.WHERE;
        return this;
    }

    public Delete whereNot(Condition condition)
    {
        where = condition;
        whereConditionType = ConditionType.WHERE_NOT;
        return this;
    }

    public Delete limit(long limit, long offset)
    {
        limitation = new Limit(limit, offset);
        return this;
    }

    public Delete limit(long limit)
    {
        return limit(limit, 0);
    }

    @Override
    public String build(Dialect dialect, Indentation indentation)
    {
        return dialect.build(this, indentation);
    }

    public static void clearWheres(Delete delete)
    {
        delete.where = null;
    }

    public static void clearLimit(Delete delete)
    {
        delete.limitation = null;
    }

    public static Delete copy(Delete delete)
    {
        return new Delete(delete);
    }
}
