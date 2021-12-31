package ltd.fdsa.database.sql.testsupport;

import lombok.var;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.columns.ColumnBuilder;
import ltd.fdsa.database.sql.conditions.Condition;
import ltd.fdsa.database.sql.dialect.DialectBuilderTestSupport;
import ltd.fdsa.database.sql.dialect.MySqlDialect;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.queries.Queries;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.utils.Indentation;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;

import java.util.function.Consumer;
import java.util.function.Function;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ColumnTestSupport<C extends Column, B extends ColumnBuilder<C, B, V>, V>
{
    protected abstract B getColumnBuilder(Table table, String name);

    private static final Table TABLE = Table.create("table");

    protected AbstractStringAssert<?> assertBuild(Consumer<B> consumer)
    {
        var columnBuilder = getColumnBuilder(TABLE, "anyColumnName");
        var indentation = Indentation.disabled();
        consumer.accept(columnBuilder);
        return Assertions.assertThat(DialectBuilderTestSupport.buildColumnDefinition(MySqlDialect.getInstance(), columnBuilder.build()
                .getColumnDefinition(), new BuildingContext(MySqlDialect.getInstance(), indentation.getDelimiter()), indentation));
    }

    protected C getOtherColumn()
    {
        return getColumnBuilder(TABLE, "other").build();
    }

    protected C getOtherColumn2()
    {
        return getColumnBuilder(TABLE, "other2").build();
    }

    protected AbstractStringAssert<?> assertCondition(Function<C, Condition> conditionFunction)
    {
        var column = getColumnBuilder(TABLE, "anyColumnName").build();
        return assertThat(Queries.select().from(TABLE).where(conditionFunction.apply(column)).build(MYSQL).substring(52));
    }
}
