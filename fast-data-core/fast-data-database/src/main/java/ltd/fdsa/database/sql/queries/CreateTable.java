package ltd.fdsa.database.sql.queries;

import ltd.fdsa.database.sql.dialect.Dialect;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.utils.Indentation;
import lombok.Getter;

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
