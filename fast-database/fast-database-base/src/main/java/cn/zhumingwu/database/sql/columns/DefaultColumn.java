package cn.zhumingwu.database.sql.columns;

import cn.zhumingwu.database.sql.columns.string.StringColumn;
import lombok.ToString;
import cn.zhumingwu.database.sql.schema.Table;

import static java.sql.Types.CHAR;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class DefaultColumn extends StringColumn<DefaultColumn> {
    DefaultColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, CHAR);
    }
}
