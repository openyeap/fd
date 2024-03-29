package cn.zhumingwu.database.sql.conditions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.utils.Indentation;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = PRIVATE)
public class CombinedCondition extends Condition
{
    private List<Condition> conditions = new ArrayList<>();

    public CombinedCondition(Condition condition)
    {
        this.conditions.add(condition);
        addPlaceholderSqlTypes(condition.getPlaceholderSqlTypes());
    }

    public void append(Condition condition)
    {
        conditions.add(condition);
        addPlaceholderSqlTypes(condition.getPlaceholderSqlTypes());
    }

    @Override
    protected String doBuild(BuildingContext context, Indentation indentation)
    {
        var builder = new StringBuilder();
        builder.append(indentation.deIndent().getIndent()).append("(");
        if (indentation.isEnabled())
        {
            builder.append(context.getDelimiter());
        }
        var isFirst = true;
        for (var condition : conditions)
        {
            if (!isFirst)
            {
                builder.append(context.getDelimiter());
            }
            builder.append(indentation.getIndent()).append(condition.build(context, !isFirst, indentation.indent()));
            isFirst = false;
        }
        if (indentation.isEnabled())
        {
            builder.append(context.getDelimiter()).append(indentation.deIndent().getIndent());
        }
        builder.append(")");
        return builder.toString();
    }

    public static Condition getCopy(Condition condition)
    {
        if (condition == null)
        {
            return null;
        }
        if (CombinedCondition.class.isAssignableFrom(condition.getClass()))
        {
            var conditions = new ArrayList<Condition>();
            for (var subCondition : ((CombinedCondition) condition).getConditions())
            {
                conditions.add(getCopy(subCondition));
            }
            var copy = new CombinedCondition();
            copy.conditions = conditions;
            copy.setType(condition.getType());
            copy.setConcatenation(condition.getConcatenation());
            copy.addPlaceholderSqlTypes(condition.getPlaceholderSqlTypes());
            return copy;
        }
        return condition;
    }
}
