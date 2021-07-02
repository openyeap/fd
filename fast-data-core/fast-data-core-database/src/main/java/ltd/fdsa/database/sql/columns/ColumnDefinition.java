package ltd.fdsa.database.sql.columns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ltd.fdsa.database.sql.domain.Size;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@Getter
@ToString
public class ColumnDefinition {
    private final String definitionName;
    private final Size size;
    private final boolean isNullable;
    private final boolean isDefaultNull;
    private final boolean isUnsigned;
    private final boolean isAutoIncrement;
    private final Object defaultValue;
}
