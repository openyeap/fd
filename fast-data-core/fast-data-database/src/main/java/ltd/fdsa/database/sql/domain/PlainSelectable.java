package ltd.fdsa.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ltd.fdsa.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class PlainSelectable implements Selectable
{
    private String value;

    @Getter
    private String alias;

    public Selectable as(String alias)
    {
        return new PlainSelectable(value, alias);
    }

    @Override
    public String getValue(BuildingContext context, Indentation indentation)
    {
        return value;
    }
}
