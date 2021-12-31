package ltd.fdsa.database.sql.domain;

import ltd.fdsa.database.sql.columns.string.VarCharColumn;
import ltd.fdsa.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import static ltd.fdsa.database.sql.domain.Placeholder.placeholder;
import static org.assertj.core.api.Assertions.assertThat;

class PlaceholderTest
{
    private static final Table TABLE = Table.create("table");

    private static final VarCharColumn COLUMN = TABLE.varCharColumn("column").build();

    @Test
    void testPlaceholder()
    {
        assertThat(((Plain) placeholder().getValue()).getValue()).isEqualTo("?");
        assertThat(((Plain) placeholder(COLUMN).getValue()).getValue()).isEqualTo(":column");
        assertThat(((Plain) placeholder("value").getValue()).getValue()).isEqualTo(":value");
    }
}
