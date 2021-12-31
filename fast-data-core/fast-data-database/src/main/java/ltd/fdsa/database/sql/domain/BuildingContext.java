package ltd.fdsa.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ltd.fdsa.database.sql.dialect.Dialect;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@Getter
@ToString
public class BuildingContext
{
    private Dialect dialect;
    private String delimiter;
}
