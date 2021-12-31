package ltd.fdsa.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;
import ltd.fdsa.database.sql.functions.Function;
import ltd.fdsa.database.sql.utils.Indentation;

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
