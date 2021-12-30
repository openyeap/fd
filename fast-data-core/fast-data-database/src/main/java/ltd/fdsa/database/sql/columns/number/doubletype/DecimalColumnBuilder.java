package ltd.fdsa.database.sql.columns.number.doubletype;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class DecimalColumnBuilder extends DoubleTypeColumnBuilder<DecimalColumnBuilder, DecimalColumn>
{
    public DecimalColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected DecimalColumn getColumnInstance()
    {
        return new DecimalColumn(table, name, null, new ColumnDefinition("DECIMAL", size, isNullable, isDefaultNull, isUnsigned, false, defaultValue));
    }
}
