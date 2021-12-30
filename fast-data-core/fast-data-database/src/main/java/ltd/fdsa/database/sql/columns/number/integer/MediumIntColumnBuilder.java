package ltd.fdsa.database.sql.columns.number.integer;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class MediumIntColumnBuilder extends IntegerColumnBuilder<MediumIntColumnBuilder, MediumIntColumn>
{
    public MediumIntColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected MediumIntColumn getColumnInstance()
    {
        return new MediumIntColumn(table, name, null, new ColumnDefinition("MEDIUMINT", size, isNullable, isDefaultNull, isUnsigned, isAutoIncrement, defaultValue));
    }
}
