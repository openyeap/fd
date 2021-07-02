package ltd.fdsa.database.sql.conditions;

import static ltd.fdsa.database.sql.conditions.GenericCondition.GenericConditionType.IS_NOT_NULL;
import static ltd.fdsa.database.sql.conditions.GenericCondition.GenericConditionType.IS_NULL;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface NullableConditions
{
    default Condition isNull()
    {
        return new GenericCondition(IS_NULL, this);
    }

    default Condition isNotNull()
    {
        return new GenericCondition(IS_NOT_NULL, this);
    }

}
