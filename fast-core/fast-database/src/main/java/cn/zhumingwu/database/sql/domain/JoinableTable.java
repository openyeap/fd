package cn.zhumingwu.database.sql.domain;

import cn.zhumingwu.database.sql.conditions.CombinedCondition;
import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.schema.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.var;
import cn.zhumingwu.database.sql.utils.BuilderUtils;
import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class JoinableTable implements Joinable {
    private final JoinType joinType;

    @Getter
    private final Table table;

    @Getter
    private final Condition condition;

    private static String buildJoin(JoinType joinType, String plainJoinable, String alias, Condition condition, BuildingContext context,
                                    Indentation indentation) {
        var builder = new StringBuilder();
        if (joinType != null) {
            builder.append(joinType.getValue()).append(" ");
        }
        builder.append("JOIN ")
                .append(plainJoinable);
        if (alias != null) {
            builder.append(" AS ").append(BuilderUtils.columnApostrophe(alias, context));
        }
        builder.append(" ON")
                .append(CombinedCondition.class.isAssignableFrom(condition.getClass()) ? context.getDelimiter() : " ")
                .append(condition.build(context, false, indentation));
        return builder.toString();
    }

    @Override
    public String getValue(BuildingContext context, Indentation indentation) {
        return buildJoin(joinType, table.getFullName(context), table.getAlias(), condition, context, indentation);
    }
}
