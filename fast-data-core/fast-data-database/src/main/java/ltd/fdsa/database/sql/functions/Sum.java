package ltd.fdsa.database.sql.functions;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.Column;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class Sum extends NumberColumnFunction
{
    public Sum(Column column)
    {
        super(column, "SUM");
    }

    public Sum(Column column, String alias)
    {
        super(column, "SUM", alias);
    }

    public Sum as(String alias)
    {
        return new Sum(column, alias);
    }

    @Override
    public int getSqlType()
    {
        return column.getSqlType();
    }
}
