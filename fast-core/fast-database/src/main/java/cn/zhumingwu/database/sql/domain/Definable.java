package cn.zhumingwu.database.sql.domain;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public interface Definable {
    ColumnDefinition getColumnDefinition();
}
