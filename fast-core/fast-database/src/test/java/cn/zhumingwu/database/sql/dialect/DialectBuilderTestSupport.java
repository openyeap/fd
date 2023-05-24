package cn.zhumingwu.database.sql.dialect;

import lombok.NoArgsConstructor;
import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.utils.Indentation;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DialectBuilderTestSupport
{
    public static <T extends DefaultDialect> String buildColumnDefinition(T dialect, ColumnDefinition definition, BuildingContext context,
            Indentation indentation)
    {
        return dialect.buildColumnDefinition(definition, context, indentation);
    }
}
