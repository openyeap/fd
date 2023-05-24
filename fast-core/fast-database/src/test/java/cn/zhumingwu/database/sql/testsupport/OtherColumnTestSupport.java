package cn.zhumingwu.database.sql.testsupport;

import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.columns.number.doubletype.DoubleColumnBuilder;
import cn.zhumingwu.database.sql.columns.number.integer.IntColumnBuilder;
import cn.zhumingwu.database.sql.schema.Table;

public interface OtherColumnTestSupport
{
    default Column getOtherColumn()
    {
        return new DoubleColumnBuilder(Table.create("table"), "other").build();
    }

    default Column getOtherColumn2()
    {
        return new IntColumnBuilder(Table.create("table"), "other2").build();
    }
}
