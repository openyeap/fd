package cn.zhumingwu.database.sql.conditions;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.domain.Placeholder;
import cn.zhumingwu.database.sql.utils.BuilderUtils;
import cn.zhumingwu.database.sql.utils.Indentation;

import java.text.MessageFormat;
import java.util.Collection;

import static java.util.stream.Collectors.joining;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@RequiredArgsConstructor
@ToString(callSuper = true)
public class IsIn extends Condition
{
    private final Object value;
    private final Collection<Object> values;

    private Placeholder placeholder;

    public IsIn(Object value, Placeholder placeholder)
    {
        this.value = value;
        this.values = null;
        this.placeholder = placeholder;
        addPlaceholderSqlTypes(resolvePlaceholderSqlTypes(value, placeholder));
    }

    @Override
    protected String doBuild(BuildingContext context, Indentation indentation)
    {
        if (values != null)
        {
            return MessageFormat.format(context.getDialect().getLabels().getIsIn(), BuilderUtils.getValued(value, context, indentation), values.stream()
                    .map(item -> BuilderUtils.getValued(item, context, indentation))
                    .collect(joining(", ")));
        }
        return MessageFormat
                .format(context.getDialect().getLabels().getIsIn(), BuilderUtils.getValued(value, context, indentation), BuilderUtils.getValued(placeholder, context, indentation));
    }
}
