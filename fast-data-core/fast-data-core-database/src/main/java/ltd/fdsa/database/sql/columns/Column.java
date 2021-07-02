package ltd.fdsa.database.sql.columns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ltd.fdsa.database.sql.conditions.EqualityConditions;
import ltd.fdsa.database.sql.conditions.NullableConditions;
import ltd.fdsa.database.sql.domain.*;
import ltd.fdsa.database.sql.domain.*;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.utils.BuilderUtils;
import ltd.fdsa.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@Getter
@ToString
public abstract class Column implements Groupable, Selectable, Definable, NullableConditions, EqualityConditions, SqlTypeSupplier {
    @ToString.Exclude
    protected Table table;
    protected String name;

    private String alias;

    protected ColumnDefinition columnDefinition;

    private int sqlType;

    @Override
    public String getValue(BuildingContext context, Indentation indentation) {
        return getFullNameOrAlias(context);
    }

    public String getFullName(BuildingContext context) {
        return table.getFullName(context) + "." + BuilderUtils.columnApostrophe(name, context);
    }

    public String getFullNameOrAlias(BuildingContext context) {
        return table.getFullNameOrAlias(context) + "." + BuilderUtils.columnApostrophe(name, context);
    }
}
