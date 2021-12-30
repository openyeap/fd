package ltd.fdsa.database.sql.dialect;

import static lombok.AccessLevel.PRIVATE;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.utils.Indentation;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class DialectBuilderTestSupport
{
    public static <T extends DefaultDialect> String buildColumnDefinition(T dialect, ColumnDefinition definition, BuildingContext context,
            Indentation indentation)
    {
        return dialect.buildColumnDefinition(definition, context, indentation);
    }
}
