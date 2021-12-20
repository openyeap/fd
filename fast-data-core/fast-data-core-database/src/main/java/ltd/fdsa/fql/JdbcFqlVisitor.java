package ltd.fdsa.fql;

import lombok.var;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.conditions.Condition;
import ltd.fdsa.database.sql.domain.LikeType;
import ltd.fdsa.database.sql.domain.OrderBy;
import ltd.fdsa.database.sql.domain.OrderDirection;
import ltd.fdsa.database.sql.queries.Queries;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.fql.antlr.FqlParser;
import ltd.fdsa.fql.model.FilterSet;
import ltd.fdsa.fql.model.QueryInfo;
import ltd.fdsa.fql.model.TableInfo;
import ltd.fdsa.fql.util.FqlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JdbcFqlVisitor {

    final FqlUtil util;

    public JdbcFqlVisitor(FqlUtil util) {
        this.util = util;
    }

    public Map<String, Object> visit(FqlParser.DocumentContext ctx) {
        var result = new HashMap<String, Object>();
        for (var selection : ctx.selectionSet().selection()) {
            visitSelection(selection, result, new HashMap<>(0));
        }
        return result;
    }

    void visitSelection(FqlParser.SelectionContext ctx, Map<String, Object> root, Map<String, Object> current) {
        var builder = QueryInfo.builder();
        List<FqlParser.SelectionContext> todoList = new ArrayList<>();
        // 表名信息
        TableInfo tableInfo = util.getTableInfo(ctx);
        builder.alias(tableInfo.getAlias()).name(tableInfo.getName()).code(tableInfo.getCode());
        // 选择信息
        if (ctx.selectionSet() != null) {
            for (var selection : ctx.selectionSet().selection()) {
                //子选择
                if (selection.arguments().size() > 0) {
                    // 延迟处理
                    todoList.add(selection);
                } else {
                    // 字段选择
                    var column = util.getColumnInfo(selection, builder.getName());
                    builder.column(column.getName(), column.getAlias(), column.getCode(), false);
                }
            }
        } else {
            var columns = util.getColumnSet(builder.getName());
            for (var column : columns.entrySet()) {
                builder.column(column.getValue());
            }
        }
        // 操作信息
        visitArguments(ctx.arguments(), builder);

        // 执行
        var queryInfo = builder.build();
        //table
        Table table = Table.create(queryInfo.getCode()).as(queryInfo.getName());
        //select
        Column[] columns = new Column[queryInfo.getColumns().size()];
        var values = queryInfo.getColumns().values();
        for (int i = 0; i < columns.length; i++) {
            var column = values[i];
            columns[i] = table.column(column.getCode()).build().as(column.getAlias());
        }
        //where
        var where = Condition.emptyCondition();
        for (var orFilter : queryInfo.getFilters()) {
            var condition = Condition.emptyCondition();
            for (var andFilter : orFilter.getFilters()) {
                switch (andFilter.getType()) {
                    case EQ:
                        if (andFilter.getValue() == null) {
                            condition = condition.and(table.column(andFilter.getName()).build().isNull());
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            condition = condition.and(table.doubleColumn(andFilter.getName()).build().eq(Double.valueOf(andFilter.getValue().toString())));
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build().eq(andFilter.getValue().toString()));
                        break;
                    case NEQ:
                        if (andFilter.getValue() == null) {
                            condition = condition.and(table.column(andFilter.getName()).build().isNotNull());
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            condition = condition.and(table.doubleColumn(andFilter.getName()).build().nEq(Double.valueOf(andFilter.getValue().toString())));
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build().nEq(andFilter.getValue().toString()));
                        break;
                    case LT:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            condition = condition.and(table.doubleColumn(andFilter.getName()).build().lt(Double.valueOf(andFilter.getValue().toString())));
                            break;
                        }
                        break;
                    case LTE:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            condition = condition.and(table.doubleColumn(andFilter.getName()).build().ltEq(Double.valueOf(andFilter.getValue().toString())));
                            break;
                        }
                        break;
                    case GT:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            condition = condition.and(table.doubleColumn(andFilter.getName()).build().gt(Double.valueOf(andFilter.getValue().toString())));
                            break;
                        }
                        break;
                    case GTE:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            condition = condition.and(table.doubleColumn(andFilter.getName()).build().gtEq(Double.valueOf(andFilter.getValue().toString())));
                            break;
                        }
                        break;
                    case LIKE:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build().isLike(andFilter.getValue().toString(), LikeType.BOTH));
                        break;
                    case START:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build().isLike(andFilter.getValue().toString(), LikeType.BEFORE));
                        break;
                    case END:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build().isLike(andFilter.getValue().toString(), LikeType.AFTER));
                        break;
                    case IN:
                        if (andFilter.getValue() == null) {
                            condition = condition.and(table.column(andFilter.getName()).build().isNull());
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            condition = condition.and(table.doubleColumn(andFilter.getName()).build().eq(Double.valueOf(andFilter.getValue().toString())));
                            break;
                        }
                        if (andFilter.getValue() instanceof List) {
                            var list = (List<String>) andFilter.getValue();
                            condition = condition.and(table.column(andFilter.getName()).build().isIn(list));
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build().eq(andFilter.getValue().toString()));
                        break;
                    case NIN:
                        if (andFilter.getValue() == null) {
                            condition = condition.and(table.column(andFilter.getName()).build().isNotNull());
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            condition = condition.and(table.doubleColumn(andFilter.getName()).build().nEq(Double.valueOf(andFilter.getValue().toString())));
                            break;
                        }
                        if (andFilter.getValue() instanceof List) {
                            var list = (List<String>) andFilter.getValue();
                            condition = condition.and(table.column(andFilter.getName()).build().isNotIn(list));
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build().nEq(andFilter.getValue().toString()));
                        break;
                }
            }
            where = where.or(condition);
        }
        //order
        var orders = new ArrayList<OrderBy>();
        for (var entry : queryInfo.getOrders().entrySet()) {
            orders.add(new OrderBy(table.column(entry.getKey()).build(), entry.getValue()));
        }
        //other
        var select = Queries
                .select(columns)
                .from(table)
                .where(where);
        if (orders.size() > 0) {
            select.orderBy(orders);
        }
        if (queryInfo.getLimit() < Integer.MAX_VALUE) {
            select.limit(queryInfo.getLimit(), queryInfo.getOffset());
        }
        if (queryInfo.isDistinct()) {
            select.distinct();
        }
        var fetchData = util.fetchData(select);
        if (fetchData.size() == 1) {
            if ("...".equals(queryInfo.getAlias())) {
                current.putAll(fetchData.get(0));
            } else {
                current.put(queryInfo.getAlias(), fetchData.get(0));
            }
            root.putAll(current);
            for (var selection : todoList) {
                this.visitSelection(selection, current, fetchData.get(0));
            }
        } else {
            var list = new ArrayList<Map<String, Object>>();
            // todo
            if ("...".equals(queryInfo.getAlias())) {
                for (var item : fetchData) {
                    item.putAll(current);
                    list.add(item);
                    // 延迟处理
                    for (var selection : todoList) {
                        this.visitSelection(selection, root, item);
                    }
                }
                root.put("", list);
            } else {
                root.put(queryInfo.getAlias(), fetchData);
            }
        }
    }

    void visitArguments(List<FqlParser.ArgumentsContext> contextList, QueryInfo.QueryBuilder builder) {
        for (var ctx : contextList) {
            if (ctx.argument() != null && !ctx.argument().isEmpty()) {
                var filters = new FilterSet();
                for (var argument : ctx.argument()) {
                    visitArgument(argument, builder, filters);
                }
                builder.filters(filters);
            }
        }
    }

    void visitArgument(FqlParser.ArgumentContext ctx, QueryInfo.QueryBuilder builder, FilterSet filters) {
        var text = ctx.name().getText();
        var lastIndexOf = text.lastIndexOf('_');
        String operator = "";
        String name = text;
        if (lastIndexOf >= 0) {
            operator = text.substring(lastIndexOf);
            name = text.substring(0, lastIndexOf);
        }

        switch (operator) {
            case "eq":
            case "gt":
            case "gte":
            case "lt":
            case "lte":
            case "neq":
            case "like":
            case "start":
            case "end":
            case "in":
            case "nin":
                if (ctx.valueWithVariable().NullValue() != null) {
                    filters.add(name, FilterSet.Type.valueOf(operator), null);
                } else if (ctx.valueWithVariable().StringValue() != null) {
                    filters.add(name, FilterSet.Type.valueOf(operator), ctx.valueWithVariable().getText());
                } else if (ctx.valueWithVariable().arrayValueWithVariable() != null) {
                    var value = ctx.valueWithVariable().arrayValueWithVariable().valueWithVariable();
                    if (value.isEmpty()) {
                        break;
                    }
                    var list = value.stream().map(c -> c.getText()).collect(Collectors.toList());
                    filters.add(name, FilterSet.Type.valueOf(operator), list);
                } else {
                    filters.add(name, FilterSet.Type.valueOf(operator), (Double.valueOf(ctx.valueWithVariable().getText())));
                }
                break;
            case "order":
                if ("desc".equals(ctx.valueWithVariable().getText())
                        || "false".equals(ctx.valueWithVariable().getText())
                ) {
                    builder.order(name, OrderDirection.DESC);
                    break;
                }
                builder.order(name, OrderDirection.ASC);
                break;
            case "limit":
                if (ctx.valueWithVariable().IntValue() != null) {
                    builder.limit(Integer.valueOf(ctx.valueWithVariable().getText()));
                }
                break;
            case "offset":
                if (ctx.valueWithVariable().IntValue() != null) {
                    builder.offset(Integer.valueOf(ctx.valueWithVariable().getText()));
                }
                break;
            case "distinct":
                builder.distinct();
                break;
        }
    }


}
