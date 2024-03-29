package cn.zhumingwu.database.sql.queries;


import cn.zhumingwu.database.sql.columns.datetime.DateColumn;
import cn.zhumingwu.database.sql.columns.datetime.DateTimeColumn;
import cn.zhumingwu.database.sql.columns.number.doubletype.DoubleColumn;
import cn.zhumingwu.database.sql.columns.number.integer.IntColumn;
import cn.zhumingwu.database.sql.columns.string.VarCharColumn;
import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.functions.Function;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static cn.zhumingwu.database.sql.dialect.Dialects.SYBASE;
import static cn.zhumingwu.database.sql.domain.LikeType.AFTER;
import static cn.zhumingwu.database.sql.queries.Queries.update;
import static cn.zhumingwu.database.sql.utils.Indentation.enabled;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateTest
{
    public static final Table PERSONS = Table.create("persons");

    public static final VarCharColumn FORENAME = PERSONS.varCharColumn("forename").build();
    public static final VarCharColumn LASTNAME = PERSONS.varCharColumn("lastname").build();
    public static final VarCharColumn NICKNAME = PERSONS.varCharColumn("nickname").build();
    public static final IntColumn AGE = PERSONS.intColumn("age").build();
    public static final DoubleColumn SIZE = PERSONS.doubleColumn("size").build();
    public static final IntColumn COUNT = PERSONS.intColumn("count").build();
    public static final DateColumn BIRTHDAY = PERSONS.dateColumn("birthday").build();
    public static final DateColumn DEATHDAY = PERSONS.dateColumn("deathday").build();
    public static final DateTimeColumn LAST_UPDATE = PERSONS.dateTimeColumn("lastUpdate").build();

    @Test
    void testBuildUpdate()
    {
        var update = update(PERSONS)
                .set(NICKNAME, FORENAME)
                .set(LASTNAME, "Schumacher")
                .set(BIRTHDAY, Function.now())
                .set(DEATHDAY, LocalDate.of(2020, 4, 24))
                .set(LAST_UPDATE, LocalDateTime.of(2020, 4, 24, 13, 53))
                .set(COUNT, Integer.valueOf(5))
                .set(AGE, 38)
                .set(SIZE, 175.89)
                .where(LASTNAME.isNotLike("Nils", AFTER)
                        .and(Function.min(AGE).isGreaterThanOrEqualTo(50))
                        .and(LASTNAME.isEqualTo("Schumacher")
                                .or(Condition.plain("IsNull(COL, '') != ''"))));

        update.println(MYSQL, enabled());
        update.println(SYBASE, enabled());

        assertThat(update.build(MYSQL))
                .isEqualTo("UPDATE `persons` SET `persons`.`nickname` = `persons`.`forename`, `persons`.`lastname` = 'Schumacher', `persons`.`birthday` = NOW(), `persons`.`deathday` = '2020-04-24', `persons`.`lastUpdate` = '2020-04-24 13:53:00.000000', `persons`.`count` = 5, `persons`.`age` = 38, `persons`.`size` = 175.89 WHERE (`persons`.`lastname` NOT LIKE 'Nils%' AND MIN(`persons`.`age`) >= 50 AND (`persons`.`lastname` = 'Schumacher' OR IsNull(COL, '') != ''))");

        assertThat(update.build(MYSQL, enabled())).isEqualTo("UPDATE\n" //
                + "  `persons`\n" //
                + "SET\n" //
                + "  `persons`.`nickname` = `persons`.`forename`,\n" //
                + "  `persons`.`lastname` = 'Schumacher',\n" //
                + "  `persons`.`birthday` = NOW(),\n" //
                + "  `persons`.`deathday` = '2020-04-24',\n" //
                + "  `persons`.`lastUpdate` = '2020-04-24 13:53:00.000000',\n" //
                + "  `persons`.`count` = 5,\n" //
                + "  `persons`.`age` = 38,\n" //
                + "  `persons`.`size` = 175.89\n" //
                + "WHERE\n" //
                + "(\n" //
                + "  `persons`.`lastname` NOT LIKE 'Nils%'\n" //
                + "  AND MIN(`persons`.`age`) >= 50\n" //
                + "  AND\n" //
                + "  (\n" //
                + "    `persons`.`lastname` = 'Schumacher'\n" //
                + "    OR IsNull(COL, '') != ''\n" //
                + "  )\n" //
                + ")");

        assertThat(update.build(MYSQL)).isEqualTo(update.build(SYBASE));
        assertThat(update.build(MYSQL, enabled())).isEqualTo(update.build(SYBASE, enabled()));

        assertThat(Update.copy(update).build(MYSQL)).isEqualTo(update.build(MYSQL));
    }

    @Test
    void testBuildUpdateWithWhereNot()
    {
        var update = update(PERSONS).set(NICKNAME, FORENAME).whereNot(LASTNAME.eq("Schumacher"));

        assertThat(update.build()).isEqualTo("UPDATE `persons` SET `persons`.`nickname` = `persons`.`forename` WHERE NOT `persons`.`lastname` = 'Schumacher'");
    }

    @Test
    void testClearWheres()
    {
        var update = update(PERSONS).set(NICKNAME, FORENAME).where(LASTNAME.eq("Schumacher"));

        assertThat(update.getWhere()).isNotNull();
        Update.clearWheres(update);
        assertThat(update.getWhere()).isNull();
    }
}
