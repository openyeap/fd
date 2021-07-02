package ltd.fdsa.database.sql.columns.datetime;

import static ltd.fdsa.database.sql.domain.Placeholder.placeholder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.BiFunction;

import ltd.fdsa.database.sql.columns.ColumnTest;
import ltd.fdsa.database.sql.domain.LikeType;
import ltd.fdsa.database.sql.domain.Placeholder;
import ltd.fdsa.database.sql.testsupport.ColumnAliasTestSupport;
import ltd.fdsa.database.sql.testsupport.Consumers;
import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.schema.Table;

class DateColumnTest extends ColumnTest<DateColumn, DateColumnBuilder, LocalDate> implements ColumnAliasTestSupport<DateColumn, DateColumnBuilder, LocalDate>
{
    @Override
    public DateColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new DateColumnBuilder(table, name);
    }

    @Override
    public BiFunction<DateColumn, String, DateColumn> getAliasFunction()
    {
        return (column, alias) -> column.as(alias);
    }

    @Test
    void testDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("DATE DEFAULT NULL");
        assertBuild(builder -> builder.notNull()).isEqualTo("DATE NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("DATE DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("DATE");
        assertBuild(builder -> builder.defaultValue(LocalDate.of(1981, 12, 29))).isEqualTo("DATE DEFAULT '1981-12-29'");
        assertBuild(builder -> builder.notNull().defaultValue(LocalDate.of(1981, 12, 29))).isEqualTo("DATE NOT NULL DEFAULT '1981-12-29'");
    }

    @Test
    void testDateColumnConditions()
    {
        assertCondition(column -> column.isEqualTo(LocalDate.of(1981, 12, 29))).isEqualTo("= '1981-12-29'");
        assertCondition(column -> column.eq(LocalDate.of(1981, 12, 29))).isEqualTo("= '1981-12-29'");
        assertCondition(column -> column.isEqualTo(toDate(LocalDate.of(1981, 12, 29)))).isEqualTo("= '1981-12-29'");
        assertCondition(column -> column.eq(toDate(LocalDate.of(1981, 12, 29)))).isEqualTo("= '1981-12-29'");
        assertCondition(column -> column.isEqualTo((LocalDate) null)).isEqualTo("IS NULL");
        assertCondition(column -> column.eq((LocalDate) null)).isEqualTo("IS NULL");
        assertCondition(column -> column.isEqualTo((Date) null)).isEqualTo("IS NULL");
        assertCondition(column -> column.eq((Date) null)).isEqualTo("IS NULL");
        assertCondition(column -> column.isEqualTo(Placeholder.placeholder())).isEqualTo("= ?");
        assertCondition(column -> column.eq(Placeholder.placeholder())).isEqualTo("= ?");
        assertCondition(column -> column.isNotEqualTo(LocalDate.of(1981, 12, 29))).isEqualTo("!= '1981-12-29'");
        assertCondition(column -> column.nEq(LocalDate.of(1981, 12, 29))).isEqualTo("!= '1981-12-29'");
        assertCondition(column -> column.isNotEqualTo(toDate(LocalDate.of(1981, 12, 29)))).isEqualTo("!= '1981-12-29'");
        assertCondition(column -> column.nEq(toDate(LocalDate.of(1981, 12, 29)))).isEqualTo("!= '1981-12-29'");
        assertCondition(column -> column.isNotEqualTo((LocalDate) null)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.nEq((LocalDate) null)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotEqualTo((Date) null)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.nEq((Date) null)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotEqualTo(Placeholder.placeholder())).isEqualTo("!= ?");
        assertCondition(column -> column.nEq(Placeholder.placeholder())).isEqualTo("!= ?");
        assertCondition(column -> column.isLike("1981-12-%")).isEqualTo("LIKE '1981-12-%'");
        assertCondition(column -> column.isLike((String) null)).isEqualTo("IS NULL");
        assertCondition(column -> column.isLike("-12-29", LikeType.BEFORE)).isEqualTo("LIKE '%-12-29'");
        assertCondition(column -> column.isLike((String) null, LikeType.BEFORE)).isEqualTo("IS NULL");
        assertCondition(column -> column.isLike("1981-12-", LikeType.AFTER)).isEqualTo("LIKE '1981-12-%'");
        assertCondition(column -> column.isLike((String) null, LikeType.AFTER)).isEqualTo("IS NULL");
        assertCondition(column -> column.isLike("-12-", LikeType.BOTH)).isEqualTo("LIKE '%-12-%'");
        assertCondition(column -> column.isLike((String) null, LikeType.BOTH)).isEqualTo("IS NULL");
        assertCondition(column -> column.isLike(getOtherColumn())).isEqualTo("LIKE `table`.`other`");
        assertCondition(column -> column.isLike(Placeholder.placeholder())).isEqualTo("LIKE ?");
        assertCondition(column -> column.isNotLike("1981-12-%")).isEqualTo("NOT LIKE '1981-12-%'");
        assertCondition(column -> column.isNotLike((String) null)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotLike("-12-29", LikeType.BEFORE)).isEqualTo("NOT LIKE '%-12-29'");
        assertCondition(column -> column.isNotLike((String) null, LikeType.BEFORE)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotLike("1981-12-", LikeType.AFTER)).isEqualTo("NOT LIKE '1981-12-%'");
        assertCondition(column -> column.isNotLike((String) null, LikeType.AFTER)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotLike("-12-", LikeType.BOTH)).isEqualTo("NOT LIKE '%-12-%'");
        assertCondition(column -> column.isNotLike((String) null, LikeType.BOTH)).isEqualTo("IS NOT NULL");
        assertCondition(column -> column.isNotLike(getOtherColumn())).isEqualTo("NOT LIKE `table`.`other`");
        assertCondition(column -> column.isNotLike(Placeholder.placeholder())).isEqualTo("NOT LIKE ?");
        assertCondition(column -> column.isAfter(LocalDate.of(1981, 12, 29))).isEqualTo("> '1981-12-29'");
        assertCondition(column -> column.isAfter(toDate(LocalDate.of(1981, 12, 29)))).isEqualTo("> '1981-12-29'");
        assertCondition(column -> column.isAfter(getOtherColumn())).isEqualTo("> `table`.`other`");
        assertCondition(column -> column.isAfter(Placeholder.placeholder())).isEqualTo("> ?");
        assertCondition(column -> column.isAfterOrEqualTo(LocalDate.of(1981, 12, 29))).isEqualTo(">= '1981-12-29'");
        assertCondition(column -> column.isAfterOrEqualTo(toDate(LocalDate.of(1981, 12, 29)))).isEqualTo(">= '1981-12-29'");
        assertCondition(column -> column.isAfterOrEqualTo(getOtherColumn())).isEqualTo(">= `table`.`other`");
        assertCondition(column -> column.isAfterOrEqualTo(Placeholder.placeholder())).isEqualTo(">= ?");
        assertCondition(column -> column.isBefore(LocalDate.of(1981, 12, 29))).isEqualTo("< '1981-12-29'");
        assertCondition(column -> column.isBefore(toDate(LocalDate.of(1981, 12, 29)))).isEqualTo("< '1981-12-29'");
        assertCondition(column -> column.isBefore(getOtherColumn())).isEqualTo("< `table`.`other`");
        assertCondition(column -> column.isBefore(Placeholder.placeholder())).isEqualTo("< ?");
        assertCondition(column -> column.isBeforeOrEqualTo(LocalDate.of(1981, 12, 29))).isEqualTo("<= '1981-12-29'");
        assertCondition(column -> column.isBeforeOrEqualTo(toDate(LocalDate.of(1981, 12, 29)))).isEqualTo("<= '1981-12-29'");
        assertCondition(column -> column.isBeforeOrEqualTo(getOtherColumn())).isEqualTo("<= `table`.`other`");
        assertCondition(column -> column.isBeforeOrEqualTo(Placeholder.placeholder())).isEqualTo("<= ?");
        assertCondition(column -> column.isBetween(LocalDate.of(1981, 12, 29), getOtherColumn())).isEqualTo("BETWEEN '1981-12-29' AND `table`.`other`");
        assertCondition(column -> column.isBetween(toDate(LocalDate.of(1981, 12, 29)), getOtherColumn())).isEqualTo("BETWEEN '1981-12-29' AND `table`.`other`");
        assertCondition(column -> column.isBetween(getOtherColumn(), getOtherColumn2())).isEqualTo("BETWEEN `table`.`other` AND `table`.`other2`");
        assertCondition(column -> column.isBetween(getOtherColumn(), LocalDate.of(2019, 12, 28))).isEqualTo("BETWEEN `table`.`other` AND '2019-12-28'");
        assertCondition(column -> column.isBetween(getOtherColumn(), toDate(LocalDate.of(2019, 12, 28)))).isEqualTo("BETWEEN `table`.`other` AND '2019-12-28'");
        assertCondition(column -> column.isBetween(LocalDate.of(1981, 12, 29), LocalDate.of(2019, 12, 28))).isEqualTo("BETWEEN '1981-12-29' AND '2019-12-28'");
        assertCondition(column -> column.isBetween(toDate(LocalDate.of(1981, 12, 29)), LocalDate.of(2019, 12, 28)))
                .isEqualTo("BETWEEN '1981-12-29' AND '2019-12-28'");
        assertCondition(column -> column.isBetween(LocalDate.of(1981, 12, 29), toDate(LocalDate.of(2019, 12, 28))))
                .isEqualTo("BETWEEN '1981-12-29' AND '2019-12-28'");
        assertCondition(column -> column.isBetween(toDate(LocalDate.of(1981, 12, 29)), toDate(LocalDate.of(2019, 12, 28))))
                .isEqualTo("BETWEEN '1981-12-29' AND '2019-12-28'");

        assertCondition(column -> column.isBetween(LocalDate.of(1981, 12, 29), Placeholder.placeholder())).isEqualTo("BETWEEN '1981-12-29' AND ?");
        assertCondition(column -> column.isBetween(toDate(LocalDate.of(1981, 12, 29)), Placeholder.placeholder())).isEqualTo("BETWEEN '1981-12-29' AND ?");
        assertCondition(column -> column.isBetween(getOtherColumn(), Placeholder.placeholder())).isEqualTo("BETWEEN `table`.`other` AND ?");
        assertCondition(column -> column.isBetween(Placeholder.placeholder(), LocalDate.of(2019, 12, 28))).isEqualTo("BETWEEN ? AND '2019-12-28'");
        assertCondition(column -> column.isBetween(Placeholder.placeholder(), toDate(LocalDate.of(2019, 12, 28)))).isEqualTo("BETWEEN ? AND '2019-12-28'");
        assertCondition(column -> column.isBetween(Placeholder.placeholder(), getOtherColumn())).isEqualTo("BETWEEN ? AND `table`.`other`");
    }

    private static final Date toDate(LocalDate localDate)
    {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
