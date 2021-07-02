package ltd.fdsa.database.sql.dialect;

import lombok.var;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.domain.Limit;
import ltd.fdsa.database.sql.domain.Valuable;
import ltd.fdsa.database.sql.domain.ValuableColumn;
import ltd.fdsa.database.sql.queries.Delete;
import ltd.fdsa.database.sql.queries.Insert;
import ltd.fdsa.database.sql.queries.Select;
import ltd.fdsa.database.sql.utils.BuilderUtils;
import ltd.fdsa.database.sql.utils.Indentation;

import java.util.Map;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public class MsSqlDialect extends DefaultDialect {
    private static final MsSqlDialect instance = new MsSqlDialect();

    static {
        Dialect.register(instance);
    }

    private MsSqlDialect() {
        // private constructor to hide the public one
    }

    @Override
    public String getName() {
        return "MsSql";
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
    public Labels getLabels() {
        return super.getLabels();
    }

    static class MsLabels implements Labels {
        @Override
        public char getColumnApostrophe() {
            return '"';
        }
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
    protected void appendLimit(StringBuilder builder, Limit limit, BuildingContext context, Indentation indentation) {
        if (limit != null) {
            builder.append(" TOP ").append(limit.getLimit());
            if (limit.getOffset() > 0) {
                builder.append(" START AT ").append(limit.getOffset() + 1);
            }
        }
    }

    public static MsSqlDialect getInstance() {
        return instance;
    }
}
