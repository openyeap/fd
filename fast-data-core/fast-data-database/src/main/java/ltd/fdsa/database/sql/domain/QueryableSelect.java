package ltd.fdsa.database.sql.domain;

import lombok.var;
import ltd.fdsa.database.sql.queries.Select;
import ltd.fdsa.database.sql.utils.Indentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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
