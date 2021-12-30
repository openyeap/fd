package ltd.fdsa.database.sql.queries;

import static ltd.fdsa.database.sql.queries.Queries.insertInto;
import static ltd.fdsa.database.sql.queries.Queries.update;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.var;
import ltd.fdsa.database.sql.testsupport.QueryExecutorTestSupport;
import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.columns.string.VarCharColumn;
import ltd.fdsa.database.sql.schema.Table;

class UpdatebleQueryTest extends QueryExecutorTestSupport
{
    private static final Table TABLE = Table.create("table");

    private static final VarCharColumn LASTNAME = TABLE.varCharColumn("lastname").build();

    @Test
    void testQuery()
    {

        var queryExecutor = new MyQueryExecutor();

        assertThat(update(TABLE).set(LASTNAME, "Schumacher").execute(queryExecutor)).isEqualTo(5);
    }

    @Test
    void testInsertQuery()
    {
        var queryExecutor = new MyQueryExecutor();

        assertThat(insertInto(TABLE).set(LASTNAME, "Schumacher").executeAndReturnKey(queryExecutor)).isEqualTo(3);
    }
}
