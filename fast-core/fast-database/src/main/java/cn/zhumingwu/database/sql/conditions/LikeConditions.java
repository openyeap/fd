package cn.zhumingwu.database.sql.conditions;

import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.domain.LikeType;
import cn.zhumingwu.database.sql.domain.Placeholder;

import static cn.zhumingwu.database.sql.conditions.GenericCondition.GenericConditionType.IS_NOT_NULL;
import static cn.zhumingwu.database.sql.conditions.GenericCondition.GenericConditionType.IS_NULL;
import static cn.zhumingwu.database.sql.domain.LikeType.NONE;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface LikeConditions
{
    default Condition isLike(CharSequence value)
    {
        return value == null ? new GenericCondition(IS_NULL, this) : new IsLike(this, value, NONE);
    }

    default Condition isLike(CharSequence value, LikeType likeType)
    {
        return value == null ? new GenericCondition(IS_NULL, this) : new IsLike(this, value, likeType);
    }

    default Condition isLike(Column otherColumn)
    {
        return new IsLike(this, otherColumn, NONE);
    }

    default Condition isLike(Placeholder placeholder)
    {
        return new IsLike(this, placeholder, NONE);
    }

    default Condition isNotLike(CharSequence value)
    {
        return value == null ? new GenericCondition(IS_NOT_NULL, this) : new IsNotLike(this, value, NONE);
    }

    default Condition isNotLike(CharSequence value, LikeType likeType)
    {
        return value == null ? new GenericCondition(IS_NOT_NULL, this) : new IsNotLike(this, value, likeType);
    }

    default Condition isNotLike(Column otherColumn)
    {
        return new IsNotLike(this, otherColumn, NONE);
    }

    default Condition isNotLike(Placeholder placeholder)
    {
        return new IsNotLike(this, placeholder, NONE);
    }
}
