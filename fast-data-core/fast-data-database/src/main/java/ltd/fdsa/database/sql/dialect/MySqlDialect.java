package ltd.fdsa.database.sql.dialect;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class MySqlDialect extends DefaultDialect
{
    private static final MySqlDialect instance;

    static
    {
        instance = new MySqlDialect(); 
		Dialects.register(instance);
    }

    private MySqlDialect()
    {
        // private constructor to hide the public one
    }

    @Override
    public String getName()
    {
        return "MySQL";
    }

    public static MySqlDialect getInstance()
    {
        return instance;
    }
}
