package ltd.fdsa.database.sql.dialect;

import static java.lang.System.getProperty;

import java.time.format.DateTimeFormatter;

import ltd.fdsa.database.sql.queries.Select;
import ltd.fdsa.database.sql.queries.Update;
import ltd.fdsa.database.sql.utils.Indentation;
import ltd.fdsa.database.sql.queries.CreateTable;
import ltd.fdsa.database.sql.queries.Delete;
import ltd.fdsa.database.sql.queries.Insert;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface Dialect
{
    String getName();

    String build(Insert insert, Indentation indentation);

    String build(Update update, Indentation indentation);

    String build(Delete delete, Indentation indentation);

    String build(Select select, Indentation indentation);

    String build(CreateTable createTable, Indentation indentation);

    DateTimeFormatter getDateFormatter();

    DateTimeFormatter getDateTimeFormatter();

    DateTimeFormatter getTimeFormatter();

    Labels getLabels();

    String escape(String value, char apostrophe);

    public static void register(Dialect dialect)
    {
        Dialects.register(dialect);
    }

    public static void unregister(String name)
    {
        Dialects.unregister(name);
    }

    public static Dialect forName(String name)
    {
        return Dialects.forName(name);
    }

    public static Dialect getDefault()
    {
        return forName(getProperty("sqlbuilder.defaultDialect", "PostgreSql"));
    }
}
