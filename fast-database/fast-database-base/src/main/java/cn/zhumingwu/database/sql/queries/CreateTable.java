package cn.zhumingwu.database.sql.queries;

import lombok.Getter;
import cn.zhumingwu.database.sql.dialect.Dialect;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@Getter
public class CreateTable implements ExecutableQuery
{
    private Table table;

    CreateTable(CreateTable createTable)
    {
        table = createTable.table;
    }

    CreateTable(Table table)
    {
        this.table = table;
    }

    @Override
    public String build(Dialect dialect, Indentation indentation)
    {
        return dialect.build(this, indentation);
    }

    public static CreateTable copy(CreateTable createTable)
    {
        return new CreateTable(createTable);
    }
}
