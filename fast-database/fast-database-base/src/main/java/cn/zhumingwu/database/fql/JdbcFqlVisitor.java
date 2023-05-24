package cn.zhumingwu.database.fql;

import cn.zhumingwu.database.fql.antlr.FqlParser;
import cn.zhumingwu.database.model.*;
import cn.zhumingwu.database.service.JdbcApiService;
import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.domain.LikeType;
import cn.zhumingwu.database.sql.domain.OrderBy;
import cn.zhumingwu.database.sql.domain.OrderDirection;
import cn.zhumingwu.database.sql.queries.Queries;
import cn.zhumingwu.database.sql.schema.Table;
import com.google.common.base.Strings;
import lombok.var;
import cn.zhumingwu.database.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class JdbcFqlVisitor {

    final JdbcApiService service;

    public JdbcFqlVisitor(JdbcApiService service) {
        this.service = service;
    }

    public Node visit(FqlParser.DocumentContext ctx) throws Exception {
        var parent = new Node(new TreeMap<>(), "data");
        for (var selection : ctx.selectionSet().selection()) {
            visitSelection(selection, parent);
        }
        return parent;
    }

    void process(Node parent, Node node) {
        if (node.isList()) {
            // 集合
            parent.getValue().put(node.getName(), node.getValues());
        } else {
            // 单体
            parent.getValue().put(node.getName(), node.getValue());
        }
    }

    TableInfo getTableInfo(FqlParser.SelectionContext ctx) {
        var alias = ctx.alias() == null ? "" : ctx.alias().name().getText();
        var name = ctx.name() == null ? "" : ctx.name().getText();
        if (Strings.isNullOrEmpty(alias)) {
            alias = name;
        }
        var ddd = service.getNamedTables().get(name);
        return new TableInfo(name, alias, ddd.getName());
    }

    ColumnInfo getColumnInfo(FqlParser.SelectionContext ctx, String table) throws Exception {
        var alias = ctx.alias() == null ? "" : ctx.alias().name().getText();
        var name = ctx.name() == null ? "" : ctx.name().getText();
        if ("...".equals(name)) {
            return new ColumnInfo("...", "...", "...", false);
        }
        if (Strings.isNullOrEmpty(alias)) {
            alias = name;
        }
        if (!this.service.getNamedColumns().containsKey(table)) {
            return null;
        }
        var column = this.service.getNamedColumns().get(table).get(name);
        if (column == null) {
            throw new Exception(name + "不存在");
        }
        return new ColumnInfo(name, alias, column.getName(), false);
    }

    ColumnSet getColumnSet(String table) {
        var columnSet = new ColumnSet();
        for (var entry : this.service.getNamedColumns().get(table).entrySet()) {

            columnSet.put(entry.getKey().toString(), entry.getKey().toString(), entry.getValue().getName(), false);
        }
        return columnSet;
    }

    void visitSelection(FqlParser.SelectionContext ctx, Node node) throws Exception {
        var builder = QueryInfo.builder();
        var todoList = new ArrayList<FqlParser.SelectionContext>();
        // 表名信息
        var tableInfo = this.getTableInfo(ctx);
        builder.alias(tableInfo.getAlias()).name(tableInfo.getName()).code(tableInfo.getCode());
        // 选择信息
        if (ctx.selectionSet() != null) {
            for (var selection : ctx.selectionSet().selection()) {
                // 子选择
                if (selection.arguments().size() > 0) {
                    // 延迟处理
                    todoList.add(selection);
                } else {
                    // 字段选择
                    var columnInfo = this.getColumnInfo(selection, builder.getName());
                    if ("...".equals(columnInfo.getName())) {
                        var columns = this.getColumnSet(builder.getName());
                        for (var column : columns.entrySet()) {
                            builder.column(column.getValue());
                        }
                    } else {
                        builder.column(columnInfo.getName(), columnInfo.getAlias(), columnInfo.getCode(), false);
                    }
                }
            }
        }
        if (builder.getColumns().size() == 0) {
            var columns = this.getColumnSet(builder.getName());
            for (var column : columns.entrySet()) {
                builder.column(column.getValue());
            }
        }
        // 操作信息
        visitArguments(ctx.arguments(), builder, node.getValue());
        // 执行
        var queryInfo = builder.build();
        // table
        var table = Table.create(queryInfo.getCode()).as(queryInfo.getName());
        // select
        Column[] columns = new Column[queryInfo.getColumns().size()];
        var values = queryInfo.getColumns().values();
        for (int i = 0; i < columns.length; i++) {
            var column = values[i];
            columns[i] = table.column(column.getCode()).alias(column.getAlias()).build();
        }
        // where
        var where = Condition.emptyCondition();
        for (var orFilter : queryInfo.getFilters()) {
            var condition = Condition.emptyCondition();
            for (var andFilter : orFilter.getFilters()) {
                switch (andFilter.getType()) {
                    case eq:
                        if (andFilter.getValue() == null) {
                            condition = condition.and(table.column(andFilter.getName()).build().isNull());
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            if (andFilter.getValue().toString().contains(".")) {
                                condition = condition.and(table.doubleColumn(andFilter.getName()).build()
                                        .eq(Double.valueOf(andFilter.getValue().toString())));
                            } else {
                                condition = condition.and(table.intColumn(andFilter.getName()).build()
                                        .eq(Long.valueOf(andFilter.getValue().toString())));
                            }
                            break;
                        }

                        condition = condition
                                .and(table.column(andFilter.getName()).build().eq(andFilter.getValue().toString()));
                        break;
                    case neq:
                        if (andFilter.getValue() == null) {
                            condition = condition.and(table.column(andFilter.getName()).build().isNotNull());
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            if (andFilter.getValue().toString().contains(".")) {
                                condition = condition.and(table.doubleColumn(andFilter.getName()).build()
                                        .nEq(Double.valueOf(andFilter.getValue().toString())));
                            } else {
                                condition = condition.and(table.intColumn(andFilter.getName()).build()
                                        .nEq(Long.valueOf(andFilter.getValue().toString())));
                            }
                            break;
                        }
                        condition = condition
                                .and(table.column(andFilter.getName()).build().nEq(andFilter.getValue().toString()));
                        break;
                    case lt:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            if (andFilter.getValue().toString().contains(".")) {
                                condition = condition.and(table.doubleColumn(andFilter.getName()).build()
                                        .lt(Double.valueOf(andFilter.getValue().toString())));
                            } else {
                                condition = condition.and(table.intColumn(andFilter.getName()).build()
                                        .lt(Long.valueOf(andFilter.getValue().toString())));
                            }
                            break;
                        }
                        break;
                    case lte:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            if (andFilter.getValue().toString().contains(".")) {
                                condition = condition.and(table.doubleColumn(andFilter.getName()).build()
                                        .ltEq(Double.valueOf(andFilter.getValue().toString())));
                            } else {
                                condition = condition.and(table.intColumn(andFilter.getName()).build()
                                        .ltEq(Long.valueOf(andFilter.getValue().toString())));

                            }
                            break;
                        }
                        break;
                    case gt:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            if (andFilter.getValue().toString().contains(".")) {
                                condition = condition.and(table.doubleColumn(andFilter.getName()).build()
                                        .gt(Double.valueOf(andFilter.getValue().toString())));
                            } else {
                                condition = condition.and(table.intColumn(andFilter.getName()).build()
                                        .gt(Long.valueOf(andFilter.getValue().toString())));
                            }
                            break;
                        }
                        break;
                    case gte:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            if (andFilter.getValue().toString().contains(".")) {
                                condition = condition.and(table.doubleColumn(andFilter.getName()).build()
                                        .gtEq(Double.valueOf(andFilter.getValue().toString())));
                            } else {
                                condition = condition.and(table.intColumn(andFilter.getName()).build()
                                        .gtEq(Long.valueOf(andFilter.getValue().toString())));
                            }
                            break;
                        }
                        break;
                    case like:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build()
                                .isLike(andFilter.getValue().toString(), LikeType.BOTH));
                        break;
                    case start:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build()
                                .isLike(andFilter.getValue().toString(), LikeType.BEFORE));
                        break;
                    case end:
                        if (andFilter.getValue() == null) {
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            break;
                        }
                        condition = condition.and(table.column(andFilter.getName()).build()
                                .isLike(andFilter.getValue().toString(), LikeType.AFTER));
                        break;
                    case in:
                        if (andFilter.getValue() == null) {
                            condition = condition.and(table.column(andFilter.getName()).build().isNull());
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            if (andFilter.getValue().toString().contains(".")) {
                                condition = condition.and(table.doubleColumn(andFilter.getName()).build()
                                        .eq(Double.valueOf(andFilter.getValue().toString())));
                            } else {
                                condition = condition.and(table.intColumn(andFilter.getName()).build()
                                        .eq(Long.valueOf(andFilter.getValue().toString())));
                            }
                            break;
                        }
                        if (andFilter.getValue() instanceof List) {
                            var list = (List<String>) andFilter.getValue();
                            condition = condition.and(table.column(andFilter.getName()).build().isIn(list));
                            break;
                        }
                        condition = condition
                                .and(table.column(andFilter.getName()).build().eq(andFilter.getValue().toString()));
                        break;
                    case nin:
                        if (andFilter.getValue() == null) {
                            condition = condition.and(table.column(andFilter.getName()).build().isNotNull());
                            break;
                        }
                        if (andFilter.getValue() instanceof Number) {
                            if (andFilter.getValue().toString().contains(".")) {
                                condition = condition.and(table.doubleColumn(andFilter.getName()).build()
                                        .nEq(Double.valueOf(andFilter.getValue().toString())));
                            } else {
                                condition = condition.and(table.intColumn(andFilter.getName()).build()
                                        .nEq(Long.valueOf(andFilter.getValue().toString())));
                            }
                            break;
                        }
                        if (andFilter.getValue() instanceof List) {
                            var list = (List<String>) andFilter.getValue();
                            condition = condition.and(table.column(andFilter.getName()).build().isNotIn(list));
                            break;
                        }
                        condition = condition
                                .and(table.column(andFilter.getName()).build().nEq(andFilter.getValue().toString()));
                        break;
                }
            }
            where = where.or(condition);
        }

        // order
        var orders = new ArrayList<OrderBy>();
        for (var entry : queryInfo.getOrders().entrySet()) {
            orders.add(new OrderBy(table.column(entry.getKey()).build(), entry.getValue()));
        }

        // other
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

        var fetchData = this.service.query(select);
        if (fetchData.size() == 1) {
            Node child = new Node(fetchData.get(0), queryInfo.getAlias());
            // 延迟处理
            for (var selection : todoList) {
                this.visitSelection(selection, child);
            }
            process(node, child);
        } else {
            final int size = fetchData.size();
            for (var i = 0; i < size; i++) {
                Node child = new Node(fetchData, queryInfo.getAlias(), i);
                // 延迟处理
                for (var selection : todoList) {
                    this.visitSelection(selection, child);
                }
                process(node, child);
            }
        }
    }

    void visitArguments(List<FqlParser.ArgumentsContext> contextList, QueryInfo.QueryBuilder builder,
            Map<String, Object> data) throws Exception {
        for (var ctx : contextList) {
            if (ctx.argument() != null && !ctx.argument().isEmpty()) {
                var filters = new FilterSet();
                for (var argument : ctx.argument()) {
                    visitArgument(argument, builder, filters, data);
                }
                builder.filters(filters);
            }
        }
    }

    void visitArgument(FqlParser.ArgumentContext ctx, QueryInfo.QueryBuilder builder, FilterSet filters,
            Map<String, Object> data) throws Exception {
        var text = ctx.name().getText();
        var lastIndexOf = text.lastIndexOf('_');
        String operator = "eq";
        String name = text;
        if (lastIndexOf >= 0) {
            operator = text.substring(lastIndexOf + 1);
            name = text.substring(0, lastIndexOf);
        }
        var column = this.service.getNamedColumns().get(builder.getName()).get(name);
        if (column == null) {
            throw new Exception(name + "不存在");
        }
        name = column.getName();
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
                var op = FilterSet.Type.valueOf(operator);
                if (ctx.valueWithVariable().NullValue() != null) {
                    filters.add(name, op, null);
                } else if (ctx.valueWithVariable().StringValue() != null) {
                    var value = ctx.valueWithVariable().StringValue().getText();
                    filters.add(name, op, value.substring(1, value.length() - 1));
                } else if (ctx.valueWithVariable().arrayValueWithVariable() != null) {
                    var value = ctx.valueWithVariable().arrayValueWithVariable().valueWithVariable();
                    if (value.isEmpty()) {
                        break;
                    }
                    var list = value.stream().map(c -> c.getText()).collect(Collectors.toList());
                    filters.add(name, op, list);
                } else if (ctx.valueWithVariable().variable() != null) {
                    var variableName = ctx.valueWithVariable().variable().name().getText();
                    if (data.containsKey(variableName)) {
                        filters.add(name, op, data.get(variableName));
                    }
                } else if (ctx.valueWithVariable().FloatValue() != null) {
                    filters.add(name, op, (Double.valueOf(ctx.valueWithVariable().getText())));
                } else {
                    filters.add(name, op, (Long.valueOf(ctx.valueWithVariable().getText())));
                }
                break;
            case "order":
                if ("desc".equals(ctx.valueWithVariable().getText())
                        || "false".equals(ctx.valueWithVariable().getText())) {
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
