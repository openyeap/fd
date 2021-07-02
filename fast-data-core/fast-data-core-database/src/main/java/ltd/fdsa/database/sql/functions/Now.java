package ltd.fdsa.database.sql.functions;

import static java.sql.Types.TIMESTAMP;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import ltd.fdsa.database.sql.conditions.DateTimeConditions;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.utils.Indentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Now implements Function, DateTimeConditions
{
    @Override
    public java.util.function.Function<ZonedDateTime, Temporal> getDateConversion()
    {
        return ZonedDateTime::toLocalDateTime;
    }

    @Getter
    private String alias;

    public Now as(@SuppressWarnings("hiding") String alias)
    {
        return new Now(alias);
    }

    @Override
    public String getValue(BuildingContext context, Indentation indentation)
    {
        return "NOW()";
    }

    @Override
    public int getSqlType()
    {
        return TIMESTAMP;
    }
}
