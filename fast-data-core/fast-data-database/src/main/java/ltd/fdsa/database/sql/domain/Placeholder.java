package ltd.fdsa.database.sql.domain;

import ltd.fdsa.database.sql.columns.Column;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public class Placeholder extends PlainValuable {
    private Placeholder(String value) {
        super(new Plain(value));
    }

    public static Placeholder placeholder(Column column) {
        return new Placeholder(":" + column.getName());
    }

    public static Placeholder placeholder() {
        return new Placeholder("?");
    }

    public static Placeholder placeholder(String name) {
        return new Placeholder(":" + name);
    }
}
