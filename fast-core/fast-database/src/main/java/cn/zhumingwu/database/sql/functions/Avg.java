package cn.zhumingwu.database.sql.functions;

import lombok.ToString;
import cn.zhumingwu.database.sql.columns.Column;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class Avg extends NumberColumnFunction
{
    public Avg(Column column)
    {
        super(column, "AVG");
    }

    public Avg(Column column, String alias)
    {
        super(column, "AVG", alias);
    }

    public Avg as(String alias)
    {
        return new Avg(column, alias);
    }

    @Override
    public int getSqlType()
    {
        return column.getSqlType();
    }
}
