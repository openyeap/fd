package ltd.fdsa.database.sql.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ltd.fdsa.database.sql.conditions.DateTimeConditions;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.utils.Indentation;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import static java.sql.Types.TIMESTAMP;

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

    public Now as(String alias)
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
