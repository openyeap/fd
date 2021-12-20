package ltd.fdsa.fql;

import com.google.common.base.Strings;
import lombok.var;
import ltd.fdsa.database.sql.conditions.Condition;
import ltd.fdsa.database.sql.conditions.EmptyCondition;
import ltd.fdsa.database.sql.domain.LikeType;
import ltd.fdsa.database.sql.domain.OrderBy;
import ltd.fdsa.database.sql.domain.OrderDirection;
import ltd.fdsa.database.sql.queries.Queries;
import ltd.fdsa.database.sql.queries.Select;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.fql.antlr.FqlParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FqlUtil {


    public List<FqlParser.SelectionContext> getSelectionSet(FqlParser.DocumentContext ctx) {
        return ctx.selectionSet().selection();
    }

    List<FqlParser.ArgumentsContext> getArgumentList(FqlParser.SelectionContext ctx) {
        if (ctx.arguments().size() == 0) {
            return Collections.emptyList();
        }
        return ctx.arguments();
    }

    public NameAndAlias getNameAndAlias(FqlParser.SelectionContext ctx) {
        var alias = ctx.alias() == null ? "" : ctx.alias().name().getText();

        var name = ctx.name() == null ? "" : ctx.name().getText();
        if (Strings.isNullOrEmpty(alias)) {
            alias = name;
        }
        return NameAndAlias.builder().alias(alias).name(name).build();
    }

    Select visitArguments(List<FqlParser.ArgumentsContext> contextList, Table table) {
        var distinct = false;
        Integer limit = 1000000;
        Integer offset = 0;

        List<OrderBy> orders = new ArrayList<>();
        Condition condition = new EmptyCondition();
        for (var ctx : contextList) {
            Condition emptyCondition = new EmptyCondition();
            if (ctx.argument() != null && !ctx.argument().isEmpty()) {
                for (var argument : ctx.argument()) {
                    distinct |= visitArgument(argument, table, limit, offset, emptyCondition, orders);
                }
            }
            condition = condition.or(emptyCondition);
        }
        var query = Queries.select().from(table);
        if (distinct) {
            query.distinct();
        }
        query.limit(limit, offset).orderBy(orders).where(condition);
        return query;
    }

    boolean visitArgument(FqlParser.ArgumentContext ctx, Table table, Integer limit, Integer offset, Condition condition, List<OrderBy> orders) {
        var data = new HashMap<String, Object>();
        var text = ctx.name().getText();


        var i = text.lastIndexOf('_');
        String operator = "";
        String name = text;
        if (i >= 0) {
            operator = text.substring(i);
            name = text.substring(0, i);
        }

        switch (operator) {
            case "":
            case "eq":
                if (ctx.valueWithVariable().NullValue() != null) {
                    condition = condition.and(table.column(name).build().isNull());
                } else if (ctx.valueWithVariable().StringValue() != null) {
                    condition = condition.and(table.column(name).build().eq(ctx.valueWithVariable().getText()));
                } else {
                    condition = condition.and(table.doubleColumn(name).build().eq(Double.valueOf(ctx.valueWithVariable().getText())));
                }
                break;
            case "gt":
                condition = condition.and(table.doubleColumn(name).build().gt(Double.valueOf(ctx.valueWithVariable().getText())));
                break;
            case "gte":
                condition = condition.and(table.doubleColumn(name).build().gtEq(Double.valueOf(ctx.valueWithVariable().getText())));
                break;
            case "lt":
                condition = condition.and(table.doubleColumn(name).build().lt(Double.valueOf(ctx.valueWithVariable().getText())));
                break;
            case "lte":
                condition = condition.and(table.doubleColumn(name).build().ltEq(Double.valueOf(ctx.valueWithVariable().getText())));
                break;
            case "neq":
                if (ctx.valueWithVariable().NullValue() != null) {
                    condition = condition.and(table.column(name).build().isNotNull());
                } else if (ctx.valueWithVariable().StringValue() != null) {
                    condition = condition.and(table.column(name).build().nEq(ctx.valueWithVariable().getText()));
                } else {
                    condition = condition.and(table.doubleColumn(name).build().nEq(Double.valueOf(ctx.valueWithVariable().getText())));
                }
                break;
            case "like":
                if (ctx.valueWithVariable().StringValue() != null) {
                    condition = condition.and(table.column(name).build().isLike(ctx.valueWithVariable().getText(), LikeType.BOTH));
                }
                break;
            case "start":
                if (ctx.valueWithVariable().StringValue() != null) {
                    condition = condition.and(table.column(name).build().isLike(ctx.valueWithVariable().getText(), LikeType.BEFORE));
                }
                break;
            case "end":
                if (ctx.valueWithVariable().StringValue() != null) {
                    condition = condition.and(table.column(name).build().isLike(ctx.valueWithVariable().getText(), LikeType.AFTER));
                }
                break;


            case "in":
                if (ctx.valueWithVariable().arrayValueWithVariable() != null) {
                    var value = ctx.valueWithVariable().arrayValueWithVariable().valueWithVariable();
                    if (value.isEmpty()) {
                        break;
                    }
                    var list = value.stream().map(c -> c.getText()).collect(Collectors.toList());
                    condition = condition.and(table.column(name).build().isIn(list));
                }
                break;
            case "asc":
                orders.add(new OrderBy(table.column(name).build(), OrderDirection.ASC));
            case "desc":
                orders.add(new OrderBy(table.column(name).build(), OrderDirection.DESC));
            case "limit":
                if (ctx.valueWithVariable().IntValue() != null) {
                    limit = Integer.valueOf(ctx.valueWithVariable().getText());
                }
            case "offset":
                if (ctx.valueWithVariable().IntValue() != null) {
                    offset = Integer.valueOf(ctx.valueWithVariable().getText());
                }
            case "distinct":
                return true;
        }
        return false;
    }
}
