package cn.zhumingwu.database.sql.testsupport;

import java.util.function.Consumer;

public abstract class Consumers
{
    public static Consumer<Object> noAction(@SuppressWarnings("unused") Object anyValue)
    {
        return value ->
        {
            // nothing to do here
        };
    }
}
