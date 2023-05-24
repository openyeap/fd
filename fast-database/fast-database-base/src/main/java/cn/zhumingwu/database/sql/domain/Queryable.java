package cn.zhumingwu.database.sql.domain;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public interface Queryable extends Valuable, Aliasable {
    static PlainQueryable plain(String value) {
        return new PlainQueryable(value, null);
    }
}
