package ltd.fdsa.database.sql.schema;

import lombok.var;
import ltd.fdsa.database.sql.columns.datetime.DateColumnBuilder;
import ltd.fdsa.database.sql.columns.datetime.DateTimeColumnBuilder;
import ltd.fdsa.database.sql.columns.number.doubletype.DecimalColumnBuilder;
import ltd.fdsa.database.sql.columns.number.doubletype.DoubleColumnBuilder;
import ltd.fdsa.database.sql.columns.number.doubletype.FloatColumnBuilder;
import ltd.fdsa.database.sql.columns.number.integer.*;
import ltd.fdsa.database.sql.columns.string.CharColumnBuilder;
import ltd.fdsa.database.sql.columns.string.TextColumnBuilder;
import ltd.fdsa.database.sql.columns.string.VarCharColumnBuilder;
import ltd.fdsa.database.sql.domain.BuildingContext;
import org.junit.jupiter.api.Test;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.utils.Indentation.disabled;
import static org.assertj.core.api.Assertions.assertThat;

class TableTest
{
    @Test
    void testGetFullNameOrAlias()
    {
        assertThat(Table.create("table").getFullNameOrAlias(new BuildingContext(MYSQL, disabled().getDelimiter()))).isEqualTo("`table`");
        assertThat(Table.create("table").as("alias").getFullNameOrAlias(new BuildingContext(MYSQL, disabled().getDelimiter()))).isEqualTo("`alias`");
    }

    @Test
    void testColumnMethods()
    {
        var table = Table.create("table");
        assertThat(table.charColumn("anyName")).isInstanceOf(CharColumnBuilder.class);
        assertThat(table.varCharColumn("anyName")).isInstanceOf(VarCharColumnBuilder.class);
        assertThat(table.textColumn("anyName")).isInstanceOf(TextColumnBuilder.class);
        assertThat(table.tinyIntColumn("anyName")).isInstanceOf(TinyIntColumnBuilder.class);
        assertThat(table.smallIntColumn("anyName")).isInstanceOf(SmallIntColumnBuilder.class);
        assertThat(table.mediumIntColumn("anyName")).isInstanceOf(MediumIntColumnBuilder.class);
        assertThat(table.intColumn("anyName")).isInstanceOf(IntColumnBuilder.class);
        assertThat(table.bigIntColumn("anyName")).isInstanceOf(BigIntColumnBuilder.class);
        assertThat(table.dateColumn("anyName")).isInstanceOf(DateColumnBuilder.class);
        assertThat(table.dateTimeColumn("anyName")).isInstanceOf(DateTimeColumnBuilder.class);
        assertThat(table.doubleColumn("anyName")).isInstanceOf(DoubleColumnBuilder.class);
        assertThat(table.floatColumn("anyName")).isInstanceOf(FloatColumnBuilder.class);
        assertThat(table.decimalColumn("anyName")).isInstanceOf(DecimalColumnBuilder.class);
    }
}
