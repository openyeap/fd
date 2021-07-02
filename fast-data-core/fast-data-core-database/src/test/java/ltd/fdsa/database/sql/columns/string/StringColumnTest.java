package ltd.fdsa.database.sql.columns.string;

import static ltd.fdsa.database.sql.domain.Placeholder.placeholder;

import java.util.Arrays;

import ltd.fdsa.database.sql.columns.ColumnTest;
import ltd.fdsa.database.sql.domain.LikeType;
import ltd.fdsa.database.sql.domain.Placeholder;
import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.schema.Table;

class StringColumnTest<C extends StringColumn<?>, B extends StringColumnBuilder<B, C>> extends ColumnTest<C, B, CharSequence>
{
    @SuppressWarnings("unchecked")
    @Override
    public B getColumnBuilder(Table table, String name)
    {
        return (B) new VarCharColumnBuilder(table, name);
    }

    @Test
    void testStringColumnConditions()
    {
        assertCondition(column -> column.isEqualTo("ABC")).isEqualTo("= 'ABC'");
        assertCondition(column -> column.eq("ABC")).isEqualTo("= 'ABC'");
        assertCondition(column -> column.isEqualTo((String) null)).isEqualTo("IS NULL");
        assertCondition(column -> column.eq((String) null)).isEqualTo("IS NULL");
        assertCondition(column -> column.isNotEqualTo("ABC")).isEqualTo("!= 'ABC'");
        assertCondition(column -> column.nEq("ABC")).isEqualTo("!= 'ABC'");
        assertCondition(column -> column.isNotEqualTo((String) null)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.nEq((String) null)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isIn("a", "b", "c")).isEqualTo("IN ('a', 'b', 'c')");
        assertCondition(column -> column.isIn(Arrays.asList("a", "b", "c"))).isEqualTo("IN ('a', 'b', 'c')");
        assertCondition(column -> column.isIn(Placeholder.placeholder())).isEqualTo("IN (?)");
        assertCondition(column -> column.isNotIn("a", "b", "c")).isEqualTo("NOT IN ('a', 'b', 'c')");
        assertCondition(column -> column.isNotIn(Arrays.asList("a", "b", "c"))).isEqualTo("NOT IN ('a', 'b', 'c')");
        assertCondition(column -> column.isNotIn(Placeholder.placeholder())).isEqualTo("NOT IN (?)");
        assertCondition(column -> column.isLike("anyValue")).isEqualTo("LIKE 'anyValue'");
        assertCondition(column -> column.isLike((String) null)).isEqualTo("IS NULL");
        assertCondition(column -> column.isLike("anyValue", LikeType.BEFORE)).isEqualTo("LIKE '%anyValue'");
        assertCondition(column -> column.isLike((String) null, LikeType.BEFORE)).isEqualTo("IS NULL");
        assertCondition(column -> column.isLike("anyValue", LikeType.AFTER)).isEqualTo("LIKE 'anyValue%'");
        assertCondition(column -> column.isLike((String) null, LikeType.AFTER)).isEqualTo("IS NULL");
        assertCondition(column -> column.isLike("anyValue", LikeType.BOTH)).isEqualTo("LIKE '%anyValue%'");
        assertCondition(column -> column.isLike((String) null, LikeType.BOTH)).isEqualTo("IS NULL");
        assertCondition(column -> column.isLike(getOtherColumn())).isEqualTo("LIKE `table`.`other`");
        assertCondition(column -> column.isLike(Placeholder.placeholder())).isEqualTo("LIKE ?");
        assertCondition(column -> column.isNotLike("anyValue")).isEqualTo("NOT LIKE 'anyValue'");
        assertCondition(column -> column.isNotLike((String) null)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotLike("anyValue", LikeType.BEFORE)).isEqualTo("NOT LIKE '%anyValue'");
        assertCondition(column -> column.isNotLike((String) null, LikeType.BEFORE)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotLike("anyValue", LikeType.AFTER)).isEqualTo("NOT LIKE 'anyValue%'");
        assertCondition(column -> column.isNotLike((String) null, LikeType.AFTER)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotLike("anyValue", LikeType.BOTH)).isEqualTo("NOT LIKE '%anyValue%'");
        assertCondition(column -> column.isNotLike((String) null, LikeType.BOTH)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotLike(getOtherColumn())).isEqualTo("NOT LIKE `table`.`other`");
        assertCondition(column -> column.isNotLike(Placeholder.placeholder())).isEqualTo("NOT LIKE ?");
    }
}
