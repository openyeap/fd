package cn.zhumingwu.database.sql.conditions;

import cn.zhumingwu.database.sql.domain.Placeholder;
import cn.zhumingwu.database.sql.utils.ArrayUtils;

import java.util.Collection;
import java.util.stream.Collectors;

import static cn.zhumingwu.database.sql.conditions.GenericCondition.GenericConditionType.*;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface StringConditions extends LikeConditions
{
    default Condition isEqualTo(String value)
    {
        return value == null ? new GenericCondition(IS_NULL, this) : new GenericCondition(IS_EQUAL_TO, this, value);
    }

    /**
     * Alias for {@link #isEqualTo(String)} for shorter statements
     *
     * @param value the value to compare equality against
     * @return the {@link Condition}
     */
    default Condition eq(String value)
    {
        return isEqualTo(value);
    }

    default Condition isNotEqualTo(String value)
    {
        return value == null ? new GenericCondition(IS_NOT_NULL, this) : new GenericCondition(IS_NOT_EQUAL_TO, this, value);
    }

    /**
     * Alias for {@link #isNotEqualTo(String)} for shorter statements
     *
     * @param value the value to compare equality against
     * @return the {@link Condition}
     */
    default Condition nEq(String value)
    {
        return isNotEqualTo(value);
    }

    default Condition isIn(Collection<String> values)
    {
        return new IsIn(this, values.stream().map(value -> (Object) value).collect(Collectors.toList()));
    }

    default Condition isIn(CharSequence value, CharSequence... furtherValues)
    {
        return new IsIn(this, ArrayUtils.toList(value, furtherValues));
    }

    default Condition isIn(Placeholder placeholder)
    {
        return new IsIn(this, placeholder);
    }

    default Condition isNotIn(Collection<String> values)
    {
        return new IsNotIn(this, values.stream().map(value -> (Object) value).collect(Collectors.toList()));
    }

    default Condition isNotIn(CharSequence value, CharSequence... furtherValues)
    {
        return new IsNotIn(this, ArrayUtils.toList(value, furtherValues));
    }

    default Condition isNotIn(Placeholder placeholder)
    {
        return new IsNotIn(this, placeholder);
    }
}
