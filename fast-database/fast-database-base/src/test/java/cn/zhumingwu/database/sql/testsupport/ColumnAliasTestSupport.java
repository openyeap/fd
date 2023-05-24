package cn.zhumingwu.database.sql.testsupport;

import lombok.var;
import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.columns.ColumnBuilder;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.sql.utils.Indentation;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static org.assertj.core.api.Assertions.assertThat;

public interface ColumnAliasTestSupport<C extends Column, B extends ColumnBuilder<C, B, V>, V>
{
    B getColumnBuilder(Table table, String name);

    BiFunction<C, String, C> getAliasFunction();

    @Test
    default void testAlias()
    {
        var table = Table.create("table");
        var context = new BuildingContext(MYSQL, Indentation.disabled().getDelimiter());
        var column = getColumnBuilder(table, "anyColumnName").build();
        assertThat(column.getAlias()).isNull();
        assertThat(column.getFullName(context)).isEqualTo("`table`.`anyColumnName`");
        assertThat(column.getFullNameOrAlias(context)).isEqualTo("`table`.`anyColumnName`");
        column = getAliasFunction().apply(column, "anyAlias");
        assertThat(column.getAlias()).isEqualTo("anyAlias");
        assertThat(column.getFullNameOrAlias(context)).isEqualTo("`table`.`anyColumnName`");
    }
}
