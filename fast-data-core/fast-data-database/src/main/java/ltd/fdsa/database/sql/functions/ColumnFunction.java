package ltd.fdsa.database.sql.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.conditions.EqualityConditions;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public abstract class ColumnFunction implements Function, EqualityConditions
{
    protected final Column column;
    protected final String definition;

    @Getter
    private String alias;

    @Override
    public String getValue(BuildingContext context, Indentation indentation)
    {
        return definition + "(" + column.getFullNameOrAlias(context) + ")";
    }
}
