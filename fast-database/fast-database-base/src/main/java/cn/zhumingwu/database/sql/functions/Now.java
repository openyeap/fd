package cn.zhumingwu.database.sql.functions;

import cn.zhumingwu.database.sql.conditions.DateTimeConditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.utils.Indentation;

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
