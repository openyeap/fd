package cn.zhumingwu.database.sql.functions;

import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.domain.Selectable;
import cn.zhumingwu.database.sql.domain.SqlTypeSupplier;
import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface Function extends Selectable, SqlTypeSupplier
{
    @Override
    String getValue(BuildingContext context, Indentation indentation);

    public static Sum sum(Column column)
    {
        return new Sum(column);
    }

    public static Min min(Column column)
    {
        return new Min(column);
    }

    public static Max max(Column column)
    {
        return new Max(column);
    }

    public static Avg avg(Column column)
    {
        return new Avg(column);
    }

    public static Count count(Column column)
    {
        return new Count(column);
    }

    public static Count count()
    {
        return new Count(null);
    }
    public static DistinctCount distinct(Column column)
    {
        return new DistinctCount(column);
    }

    public static Now now()
    {
        return new Now();
    }
}
