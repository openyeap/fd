package ltd.fdsa.database.sql.queries;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ltd.fdsa.database.sql.queryexecutor.SelectQueryExecutor;
import ltd.fdsa.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ltd.fdsa.database.sql.queries.Queries.select;
import static org.assertj.core.api.Assertions.assertThat;

class QueryableQueryTest
{
    private static final Table TABLE = Table.create("table");
    private SelectQueryExecutor<MyClass> selectQueryExecutor = new MySelectQueryExecutor();

    @Test
    void testQuery()
    {
        assertThat(select().from(TABLE).query(selectQueryExecutor))
                .containsExactly(new MyClass("anyValue1"), new MyClass("anyValue2"), new MyClass("anyValue3"));
        assertThat(select().from(TABLE).queryOne(selectQueryExecutor)).contains(new MyClass("anyValue"));
        assertThat(select().from(TABLE).queryExactOne(selectQueryExecutor)).isEqualTo(new MyClass("anyOtherValue"));
    }

    private class MySelectQueryExecutor implements SelectQueryExecutor<MyClass>
    {
        @Override
        public List<MyClass> query(Query select)
        {
            return new ArrayList<>(Arrays.asList(new MyClass("anyValue1"), new MyClass("anyValue2"), new MyClass("anyValue3")));
        }

        @Override
        public Optional<MyClass> queryOne(Query select)
        {
            return Optional.of(new MyClass("anyValue"));
        }

        @Override
        public MyClass queryExactOne(Query query)
        {
            return new MyClass("anyOtherValue");
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private class MyClass
    {
        private String value;
    }
}
