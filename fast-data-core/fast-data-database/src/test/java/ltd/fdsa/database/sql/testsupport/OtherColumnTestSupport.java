package ltd.fdsa.database.sql.testsupport;

import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.columns.number.doubletype.DoubleColumnBuilder;
import ltd.fdsa.database.sql.columns.number.integer.IntColumnBuilder;
import ltd.fdsa.database.sql.schema.Table;

public interface OtherColumnTestSupport
{
    default Column getOtherColumn()
    {
        return new DoubleColumnBuilder(Table.create("table"), "other").build();
    }

    default Column getOtherColumn2()
    {
        return new IntColumnBuilder(Table.create("table"), "other2").build();
    }
}
