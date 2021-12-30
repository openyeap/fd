package ltd.fdsa.database.sql.conditions;

import java.text.MessageFormat;

import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.domain.LikeType;
import ltd.fdsa.database.sql.utils.BuilderUtils;
import ltd.fdsa.database.sql.utils.Indentation;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class IsLike extends Condition
{
    private Object value1;
    private Object value2;
    private LikeType likeType;

    public IsLike(Object value1, Object value2, LikeType likeType)
    {
        this.value1 = value1;
        this.value2 = value2;
        this.likeType = likeType;
        addPlaceholderSqlTypes(resolvePlaceholderSqlTypes(value1, value2));
    }

    @Override
    protected String doBuild(BuildingContext context, Indentation indentation)
    {
        return MessageFormat
                .format(context.getDialect()
                        .getLabels()
                        .getIsLike(), BuilderUtils.getValued(value1, context, indentation), BuilderUtils.buildLikePart(value2, likeType, context, indentation));
    }
}
