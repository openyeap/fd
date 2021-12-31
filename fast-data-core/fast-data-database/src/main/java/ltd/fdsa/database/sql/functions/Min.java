package ltd.fdsa.database.sql.functions;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.Column;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class Min extends NumberColumnFunction
{
    public Min(Column column)
    {
        super(column, "MIN");
    }

    public Min(Column column, String alias)
    {
        super(column, "MIN", alias);
    }

    public Min as(String alias)
    {
        return new Min(column, alias);
    }

    @Override
    public int getSqlType()
    {
        return column.getSqlType();
    }
}
