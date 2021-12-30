package ltd.fdsa.database.sql.columns.number.doubletype;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import ltd.fdsa.database.sql.columns.number.NumberColumn;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public abstract class DoubleTypeColumn<T extends DoubleTypeColumn<T>> extends NumberColumn<T, Double>
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0");

    static
    {
        FORMAT.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.GERMAN));
    }

    public DoubleTypeColumn(Table table, String name, String alias, ColumnDefinition columnDefinition, int sqlType)
    {
        super(table, name, alias, columnDefinition, sqlType);
    }
}
