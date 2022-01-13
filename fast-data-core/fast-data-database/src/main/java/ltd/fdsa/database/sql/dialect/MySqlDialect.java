package ltd.fdsa.database.sql.dialect;

import lombok.var;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.conditions.CombinedCondition;
import ltd.fdsa.database.sql.conditions.Condition;
import ltd.fdsa.database.sql.conditions.EmptyCondition;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.domain.ConditionType;
import ltd.fdsa.database.sql.domain.Valuable;
import ltd.fdsa.database.sql.domain.ValuableColumn;
import ltd.fdsa.database.sql.queries.Delete;
import ltd.fdsa.database.sql.queries.Insert;
import ltd.fdsa.database.sql.utils.BuilderUtils;
import ltd.fdsa.database.sql.utils.Indentation;

import java.util.Map;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public class MySqlDialect extends DefaultDialect {
    private static final MySqlDialect instance;

    static {
        instance = new MySqlDialect();
        Dialects.register(instance);
    }

    private MySqlDialect() {
        // private constructor to hide the public one
    }

    @Override
    public String getName() {
        return "MySQL";
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
    protected void appendDeleteStatement(StringBuilder builder, Delete delete, BuildingContext context, Indentation indentation) {
        builder.append(context.getDialect().getLabels().getDelete())
                .append(" ").append(BuilderUtils.columnApostrophe(delete.getTable().getAlias(), context))
                .append(" ").append(context.getDialect().getLabels().getFrom())
                .append(indentation.getDelimiter());

        builder.append(indentation.indent().getIndent()).append(delete.getTable().getFullName(context));
        appendAlias(builder, delete.getTable().getAlias(), context);
        appendConditions(context.getDialect().getLabels().getWhere(), builder, delete.getWhere(), delete.getWhereConditionType(), context, indentation);
        appendLimit(builder, delete.getLimitation(), context, indentation);
    }

    protected void appendConditions(String keyword, StringBuilder builder, Condition condition, ConditionType whereConditionType, BuildingContext context, Indentation indentation) {
        if (condition != null && !EmptyCondition.class.isAssignableFrom(condition.getClass())) {
            builder.append(context.getDelimiter())
                    .append(indentation.getIndent())
                    .append(keyword);
            if (whereConditionType == ConditionType.WHERE_NOT) {
                builder.append(" ").append(context.getDialect().getLabels().getNot());
            }
            builder.append(CombinedCondition.class.isAssignableFrom(condition.getClass()) ? context.getDelimiter() : " ")
                    .append(indentation.getIndent())
                    .append(condition.build(context, false, indentation.indent()));
        }
    }



    public static MySqlDialect getInstance() {
        return instance;
    }
}
