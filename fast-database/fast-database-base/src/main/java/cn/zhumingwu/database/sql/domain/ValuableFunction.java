package cn.zhumingwu.database.sql.domain;

import cn.zhumingwu.database.sql.functions.Function;
import lombok.AllArgsConstructor;
import lombok.ToString;
import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class ValuableFunction implements Valuable
{
    private Function function;

    @Override
    public String getValue(BuildingContext context, Indentation indentation)
    {
        return function.getValue(context, indentation);
    }
}
