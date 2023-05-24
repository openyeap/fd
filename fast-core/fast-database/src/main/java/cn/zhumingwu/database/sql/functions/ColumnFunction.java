package cn.zhumingwu.database.sql.functions;

import cn.zhumingwu.database.sql.conditions.EqualityConditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.utils.Indentation;

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
