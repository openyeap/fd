package ltd.fdsa.database.sql.conditions;

import lombok.ToString;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.domain.LikeType;
import ltd.fdsa.database.sql.utils.BuilderUtils;
import ltd.fdsa.database.sql.utils.Indentation;

import java.text.MessageFormat;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class IsNotLike extends Condition
{
    private Object value1;
    private Object value2;
    private LikeType likeType;

    public IsNotLike(Object value1, Object value2, LikeType likeType)
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
                        .getIsNotLike(), BuilderUtils.getValued(value1, context, indentation), BuilderUtils.buildLikePart(value2, likeType, context, indentation));
    }
}
