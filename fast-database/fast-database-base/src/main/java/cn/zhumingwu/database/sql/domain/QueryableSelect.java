package cn.zhumingwu.database.sql.domain;

import cn.zhumingwu.database.sql.queries.Select;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class QueryableSelect implements Queryable
{
    private Select select;

    @Getter
    private String alias;

    @Override
    public String getValue(BuildingContext context, Indentation indentation)
    {
        var builder = new StringBuilder();
        builder.append("(");
        if (indentation.isEnabled())
        {
            builder.append(context.getDelimiter());
        }
        builder.append(select.build(context.getDialect(), indentation.indent()));
        if (indentation.isEnabled())
        {
            builder.append(context.getDelimiter()).append(indentation.deIndent().getIndent());
        }
        builder.append(")");
        return builder.toString();
    }
}
