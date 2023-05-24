package cn.zhumingwu.database.sql.testsupport;

import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.schema.Table;
import org.assertj.core.api.AbstractStringAssert;

import java.util.function.Supplier;

import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static cn.zhumingwu.database.sql.queries.Queries.select;
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
