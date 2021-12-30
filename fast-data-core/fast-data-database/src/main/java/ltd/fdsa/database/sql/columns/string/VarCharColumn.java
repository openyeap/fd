package ltd.fdsa.database.sql.columns.string;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;

import static java.sql.Types.VARCHAR;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class VarCharColumn extends StringColumn<VarCharColumn> {
    VarCharColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, VARCHAR);
    }
}
