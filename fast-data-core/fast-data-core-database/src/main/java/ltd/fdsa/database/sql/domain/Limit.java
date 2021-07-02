package ltd.fdsa.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class Limit {
    private final long value;

    @Getter
    private final long offset;

    public long getLimit() {
        return value;
    }
}
