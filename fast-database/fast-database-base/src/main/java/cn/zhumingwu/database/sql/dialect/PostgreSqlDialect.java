package cn.zhumingwu.database.sql.dialect;

import cn.zhumingwu.database.sql.queries.Delete;
import cn.zhumingwu.database.sql.queries.Insert;
import cn.zhumingwu.database.sql.queries.Select;
import cn.zhumingwu.database.sql.queries.Update;

import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.domain.Limit;
import cn.zhumingwu.database.sql.domain.Valuable;
import cn.zhumingwu.database.sql.domain.ValuableColumn;
import cn.zhumingwu.database.sql.utils.BuilderUtils;
import cn.zhumingwu.database.sql.utils.Indentation;

import java.util.Map;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public class PostgreSqlDialect extends DefaultDialect {
    private static final PostgreSqlDialect instance = new PostgreSqlDialect();

    static {
        Dialect.register(instance);
    }

    private PostgreSqlDialect() {
        // private constructor to hide the public one
    }

    @Override
    public String getName() {
        return "PostgreSql";
    }

    @Override
    public Labels getLabels() {
        return new PostgreLabels();
    }

    static class PostgreLabels implements Labels {
        @Override
        public char getColumnApostrophe() {
            return '"';
        }
    }

    @Override
    public String escape(String value, char apostrophe) {
        return value.replace(String.valueOf(apostrophe), String.valueOf(apostrophe) + String.valueOf(apostrophe));
    }

    @Override
    protected void appendInsertStatement(StringBuilder builder, Insert insert, BuildingContext context, Indentation indentation) {
        builder.append(context.getDialect().getLabels().getInsertInto()).append(" ").append(insert.getTable().getFullName(context));
        builder.append(indentation.getDelimiter());
        appendInsertColumns(builder, insert.getValues(), context, indentation.indent());
        appendInsertValues(builder, insert.getValues(), context, indentation.indent());
    }

    protected void appendInsertColumns(StringBuilder builder, Map<Column, Valuable> values, BuildingContext context, Indentation indentation) {
        var counter = 0;
        builder.append(indentation.getIndent()).append("(");
        for (var entry : values.entrySet()) {
            var column = entry.getKey();
            builder.append(BuilderUtils.columnApostrophe(column.getName(), context));
            if (++counter < values.size()) {
                builder.append(", ");
            }
        }
        builder.append(")").append(indentation.getDelimiter());
    }

    protected void appendInsertValues(StringBuilder builder, Map<Column, Valuable> values, BuildingContext context, Indentation indentation) {
        var counter = 0;
        builder.append("VALUES").append(indentation.getDelimiter());
        builder.append(indentation.getIndent()).append("(");
        for (var entry : values.entrySet()) {
            var value = entry.getValue();
            if (ValuableColumn.class.isAssignableFrom(value.getClass())) {
                builder.append(BuilderUtils.columnApostrophe(((ValuableColumn) value).getName(), context));
            } else {
                builder.append(value.getValue(context, indentation));
            }
            if (++counter < values.size()) {
                builder.append(", ");
            }
        }
        builder.append(")");
    }

    @Override
    protected void appendSelectStatement(StringBuilder builder, Select select, BuildingContext context, Indentation indentation) {
        builder.append(context.getDialect().getLabels().getSelect());
        appendLimit(builder, select.getLimitation(), context, indentation);
        appendDistinct(builder, select.isDistinct(), context, indentation);
        appendSelectables(builder, select.getSelectables(), context, indentation);
        appendQueryables(builder, select.getFrom(), context, indentation);
        appendJoins(builder, select.getJoins(), context, indentation);
        appendConditions(context.getDialect().getLabels().getWhere(), builder, select.getWhere(), select.getWhereConditionType(), context, indentation);
        appendGrouping(builder, select.getGroupBys(), context, indentation);
        appendConditions(context.getDialect().getLabels().getHaving(), builder, select.getHaving(), select.getHavingConditionType(), context, indentation);
        appendOrdering(builder, select.getOrderBys(), context, indentation);
    }

    @Override
    protected void appendDeleteStatement(StringBuilder builder, Delete delete, BuildingContext context, Indentation indentation) {
        builder.append(context.getDialect().getLabels().getDelete());
        if (delete.getLimitation() == null) {
            builder.append(" ");
        }
        appendLimit(builder, delete.getLimitation(), context, indentation);
        if (delete.getLimitation() != null) {
            builder.append(" ");
        }
        builder.append(context.getDialect().getLabels().getFrom()).append(indentation.getDelimiter());
        builder.append(indentation.indent().getIndent()).append(delete.getTable().getFullName(context));
        appendConditions(context.getDialect().getLabels().getWhere(), builder, delete.getWhere(), delete.getWhereConditionType(), context, indentation);
    }

    @Override
    protected void appendUpdateStatement(StringBuilder builder, Update update, BuildingContext context, Indentation indentation) {
        builder.append(context.getDialect().getLabels().getUpdate()).append(indentation.getDelimiter());
        builder.append(indentation.indent().getIndent()).append(update.getTable().getFullName(context));
//        appendAlias(builder, update.getTable().getAlias(), context);
        builder.append(indentation.getDelimiter());
        builder.append(context.getDialect().getLabels().getSet()).append(indentation.getDelimiter());
        appendUpdateValues(builder, update.getValues(), context, indentation.indent());
        appendConditions(context.getDialect().getLabels().getWhere(), builder, update.getWhere(), update.getWhereConditionType(), context, indentation);
    }

    @Override
    protected void appendUpdateValues(StringBuilder builder, Map<Column, Valuable> values, BuildingContext context, Indentation indentation) {
        var counter = 0;
        for (var entry : values.entrySet()) {
            var column = entry.getKey();
            var value = entry.getValue();
            builder.append(indentation.getIndent())
                    .append(column.getFullName(context))
                    .append(" = ")
                    .append(value.getValue(context, indentation));
            if (++counter < values.size()) {
                builder.append(",").append(indentation.getDelimiter());
            }
        }
    }

    @Override
    protected void appendLimit(StringBuilder builder, Limit limit, BuildingContext context, Indentation indentation) {
        if (limit != null) {
            builder.append(" TOP ").append(limit.getLimit());
            if (limit.getOffset() > 0) {
                builder.append(" START AT ").append(limit.getOffset() + 1);
            }
        }
    }

    public static PostgreSqlDialect getInstance() {
        return instance;
    }
}
