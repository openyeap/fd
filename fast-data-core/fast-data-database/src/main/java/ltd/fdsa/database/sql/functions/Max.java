package ltd.fdsa.database.sql.functions;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.Column;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class Max extends NumberColumnFunction
{
    public Max(Column column)
    {
        super(column, "MAX");
    }

    public Max(Column column, String alias)
    {
        super(column, "MAX", alias);
    }

    public Max as(String alias)
    {
        return new Max(column, alias);
    }

    @Override
    public int getSqlType()
    {
        return column.getSqlType();
    }
}
