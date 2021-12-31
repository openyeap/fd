package ltd.fdsa.database.sql.testsupport;

import ltd.fdsa.database.sql.conditions.Condition;
import ltd.fdsa.database.sql.schema.Table;
import org.assertj.core.api.AbstractStringAssert;

import java.util.function.Supplier;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.queries.Queries.select;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class FunctionConditionTestSupport implements OtherColumnTestSupport
{
    private static final Table TABLE = Table.create("table");

    protected AbstractStringAssert<?> assertCondition(Supplier<Condition> functionFunction)
    {
        return assertThat(select()
                .from(TABLE)
                .where(functionFunction.get())
                .build(MYSQL)
                .substring(28));
    }
}
