package ltd.fdsa.database.sql.domain;

import ltd.fdsa.database.sql.utils.Indentation;
import ltd.fdsa.database.sql.columns.Column;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class ValuableColumn implements Valuable
{
    private Column column;

    @Override
    public String getValue(BuildingContext context, Indentation indentation)
    {
        return column.getFullNameOrAlias(context);
    }

    public String getName()
    {
        return column.getName();
    }
}
