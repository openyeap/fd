package cn.zhumingwu.database.sql.domain;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public interface Selectable extends Valuable, Aliasable {
    static PlainSelectable plain(String value) {
        return new PlainSelectable(value, null);
    }
}
