package ltd.fdsa.database.sql.testsupport;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.queries.Queries.select;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import ltd.fdsa.database.sql.conditions.Condition;
import org.assertj.core.api.AbstractStringAssert;

import ltd.fdsa.database.sql.schema.Table;

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
