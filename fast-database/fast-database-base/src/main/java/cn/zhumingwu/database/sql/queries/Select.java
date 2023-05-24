package cn.zhumingwu.database.sql.queries;

import cn.zhumingwu.database.sql.conditions.CombinedCondition;
import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.domain.*;
import lombok.Getter;
import lombok.ToString;
import lombok.var;
import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.dialect.Dialect;
import cn.zhumingwu.database.sql.domain.*;
import cn.zhumingwu.database.sql.utils.Indentation;

import java.util.ArrayList;
import java.util.List;

import static cn.zhumingwu.database.sql.domain.ConditionType.WHERE;
import static cn.zhumingwu.database.sql.domain.ConditionType.WHERE_NOT;
import static cn.zhumingwu.database.sql.domain.JoinType.*;
import static cn.zhumingwu.database.sql.domain.OrderDirection.ASC;
import static cn.zhumingwu.database.sql.domain.OrderDirection.DESC;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@Getter
@ToString
public class Select implements QueryableQuery
{
    private boolean distinct;
    private List<Selectable> selectables;
    private Queryable from;
    private List<Joinable> joins;
    private Condition where;
    private ConditionType whereConditionType;
    private List<Groupable> groupBys;
    private Condition having;
    private ConditionType havingConditionType;
    private List<OrderBy> orderBys;
    private Limit limitation;

    Select(Selectable... selectables)
    {
        for (Selectable selectable : selectables)
        {
            select(selectable);
        }
    }

    Select(Select select)
    {
        distinct = select.distinct;
        selectables = select.selectables != null ? new ArrayList<>(select.selectables) : null;
        from = select.from;
        joins = select.joins != null ? new ArrayList<>(select.joins) : null;
        where = CombinedCondition.getCopy(select.where);
        whereConditionType = select.whereConditionType;
        groupBys = select.groupBys != null ? new ArrayList<>(select.groupBys) : null;
        having = CombinedCondition.getCopy(select.having);
        havingConditionType = select.havingConditionType;
        orderBys = select.orderBys != null ? new ArrayList<>(select.orderBys) : null;
        limitation = select.limitation;
    }

    public Select distinct()
    {
        return distinct(true);
    }

    public Select distinct(boolean doDistinct)
    {
        distinct = doDistinct;
        return this;
    }

    public Select select(Selectable... furtherSelectables)
    {
        for (var furtherSelectable : furtherSelectables)
        {
            select(furtherSelectable);
        }
        return this;
    }

    private Select select(Selectable selectable)
    {
        if (selectables == null)
        {
            selectables = new ArrayList<>();
        }
        selectables.add(selectable);
        return this;
    }

    public Select from(Queryable table)
    {
        from = table;
        return this;
    }

    public QueryableSelect as(String alias)
    {
        return new QueryableSelect(this, alias);
    }

    public Select join(JoinableTable table)
    {
        return addJoin(new JoinableTable(null, table.getTable(), table.getCondition()));
    }

    public Select leftJoin(JoinableTable table)
    {
        return addJoin(new JoinableTable(LEFT, table.getTable(), table.getCondition()));
    }

    public Select rightJoin(JoinableTable table)
    {
        return addJoin(new JoinableTable(RIGHT, table.getTable(), table.getCondition()));
    }

    public Select innerJoin(JoinableTable table)
    {
        return addJoin(new JoinableTable(INNER, table.getTable(), table.getCondition()));
    }

    public Select leftOuterJoin(JoinableTable table)
    {
        return addJoin(new JoinableTable(LEFT_OUTER, table.getTable(), table.getCondition()));
    }

    public Select rightOuterJoin(JoinableTable table)
    {
        return addJoin(new JoinableTable(RIGHT_OUTER, table.getTable(), table.getCondition()));
    }

    public Select fullOuterJoin(JoinableTable table)
    {
        return addJoin(new JoinableTable(FULL_OUTER, table.getTable(), table.getCondition()));
    }

    public Select where(Condition condition)
    {
        where = condition;
        whereConditionType = WHERE;
        return this;
    }

    public Select whereNot(Condition condition)
    {
        where = condition;
        whereConditionType = WHERE_NOT;
        return this;
    }

    private Select addJoin(Joinable join)
    {
        if (joins == null)
        {
            joins = new ArrayList<>();
        }
        joins.add(join);
        return this;
    }

    public Select groupBy(Column... columns)
    {
        for (var column : columns)
        {
            addGroupBy(column);
        }
        return this;
    }

    public Select groupBy(String... plainGroupBys)
    {
        for (var plainGroupBy : plainGroupBys)
        {
            addGroupBy(new PlainGroupable(plainGroupBy));
        }
        return this;
    }

    private Select addGroupBy(Groupable groupable)
    {
        if (groupBys == null)
        {
            groupBys = new ArrayList<>();
        }
        groupBys.add(groupable);
        return this;
    }

    public Select having(Condition condition)
    {
        having = condition;
        havingConditionType = WHERE;
        return this;
    }

    public Select havingNot(Condition condition)
    {
        having = condition;
        havingConditionType = WHERE_NOT;
        return this;
    }

    public Select orderBy(Column column)
    {
        return orderAscendingBy(column);
    }

    public Select orderAscendingBy(Column column) {
        return orderBy(column, ASC);
    }

    public Select orderDescendingBy(Column column) {
        return orderBy(column, DESC);
    }

    public Select orderBy(Column column, OrderDirection direction) {
        if (orderBys == null) {
            orderBys = new ArrayList<>();
        }
        orderBys.add(new OrderBy(column, direction));
        return this;
    }

    public Select orderBy(List<OrderBy> orders) {
        if (orderBys == null) {
            orderBys = new ArrayList<>();
        }
        orderBys.addAll(orders);
        return this;
    }

    public Select limit(long limit, long offset) {
        limitation = new Limit(limit, offset);
        return this;
    }

    public Select limit(long limit) {
        return limit(limit, 0);
    }

    @Override
    public String build(Dialect dialect, Indentation indentation)
    {
        return dialect.build(this, indentation);
    }

    public static void clearSelects(Select select)
    {
        select.selectables = null;
    }

    public static void clearJoins(Select select)
    {
        select.joins = null;
    }

    public static void clearWheres(Select select)
    {
        select.where = null;
    }

    public static void clearGroupBys(Select select)
    {
        select.groupBys = null;
    }

    public static void clearHavings(Select select)
    {
        select.having = null;
    }

    public static void clearOrdering(Select select)
    {
        select.orderBys = null;
    }

    public static void clearLimit(Select select)
    {
        select.limitation = null;
    }

    public static Select copy(Select select)
    {
        return new Select(select);
    }
}
