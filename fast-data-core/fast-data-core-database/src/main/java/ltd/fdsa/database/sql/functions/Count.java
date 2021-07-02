package ltd.fdsa.database.sql.functions;

import static java.sql.Types.INTEGER;

import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.utils.Indentation;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class Count extends NumberColumnFunction
{
    public Count(Column column)
    {
        super(column, "COUNT");
    }

    public Count(Column column, String alias)
    {
        super(column, "COUNT", alias);
    }

    public Count as(String alias)
    {
        return new Count(column, alias);
    }

    @Override
    public String getValue(BuildingContext context, Indentation indentation)
    {
        return definition + "(" + (column != null ? column.getFullNameOrAlias(context) : "*") + ")";
    }

    @Override
    public int getSqlType()
    {
        return INTEGER;
    }
}
