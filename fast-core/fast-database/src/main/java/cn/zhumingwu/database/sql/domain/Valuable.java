package cn.zhumingwu.database.sql.domain;

import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public interface Valuable {
    static PlainValuable plain(String value) {
        return new PlainValuable(new Plain(value));
    }

    String getValue(BuildingContext context, Indentation indentation);
}
