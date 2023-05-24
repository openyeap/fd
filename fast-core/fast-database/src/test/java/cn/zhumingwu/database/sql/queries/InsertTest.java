package cn.zhumingwu.database.sql.queries;

import lombok.var;
import cn.zhumingwu.database.sql.columns.datetime.DateColumn;
import cn.zhumingwu.database.sql.columns.datetime.DateTimeColumn;
import cn.zhumingwu.database.sql.columns.datetime.TimeColumn;
import cn.zhumingwu.database.sql.columns.number.doubletype.DoubleColumn;
import cn.zhumingwu.database.sql.columns.number.integer.BigIntColumn;
import cn.zhumingwu.database.sql.columns.number.integer.IntColumn;
import cn.zhumingwu.database.sql.columns.string.VarCharColumn;
import cn.zhumingwu.database.sql.domain.Placeholder;
import cn.zhumingwu.database.sql.functions.Function;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static cn.zhumingwu.database.sql.dialect.Dialects.SYBASE;
import static cn.zhumingwu.database.sql.queries.Queries.insertInto;
import static cn.zhumingwu.database.sql.utils.Indentation.enabled;
import static org.assertj.core.api.Assertions.assertThat;

class InsertTest
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
    public static final TimeColumn LUNCH = PERSONS.timeColumn("lunch").build();
    public static final DateTimeColumn LAST_UPDATE = PERSONS.dateTimeColumn("lastUpdate").build();
    public static final BigIntColumn NUMBERS = PERSONS.bigIntColumn("numbers").build();

    @Test
    void testBuildInsert()
    {
        var insert = insertInto(PERSONS)
                .set(NICKNAME, FORENAME)
                .set(FORENAME, "Martin")
                .set(BIRTHDAY, Function.now())
                .set(DEATHDAY, LocalDate.of(2020, 4, 24))
                .set(LUNCH, LocalTime.of(14, 46))
                .set(LAST_UPDATE, LocalDateTime.of(2020, 4, 24, 13, 53))
                .set(COUNT, Integer.valueOf(5))
                .set(AGE, 38)
                .set(SIZE, 175.89)
                .set(NUMBERS, Placeholder.placeholder(NUMBERS))
                .set(LASTNAME, "Schumacher");

        insert.println(MYSQL, enabled());
        insert.println(SYBASE, enabled());

        assertThat(insert.build())
                .isEqualTo("INSERT INTO `persons` SET `persons`.`nickname` = `persons`.`forename`, `persons`.`forename` = 'Martin', `persons`.`birthday` = NOW(), `persons`.`deathday` = '2020-04-24', `persons`.`lunch` = '14:46:00.000000', `persons`.`lastUpdate` = '2020-04-24 13:53:00.000000', `persons`.`count` = 5, `persons`.`age` = 38, `persons`.`size` = 175.89, `persons`.`numbers` = :numbers, `persons`.`lastname` = 'Schumacher'");

        assertThat(insert.build(enabled()))
                .isEqualTo("INSERT INTO\n" //
                        + "  `persons`\n" //
                        + "SET\n" //
                        + "  `persons`.`nickname` = `persons`.`forename`,\n" //
                        + "  `persons`.`forename` = 'Martin',\n" //
                        + "  `persons`.`birthday` = NOW(),\n" //
                        + "  `persons`.`deathday` = '2020-04-24',\n" //
                        + "  `persons`.`lunch` = '14:46:00.000000',\n" //
                        + "  `persons`.`lastUpdate` = '2020-04-24 13:53:00.000000',\n" //
                        + "  `persons`.`count` = 5,\n" //
                        + "  `persons`.`age` = 38,\n" //
                        + "  `persons`.`size` = 175.89,\n" //
                        + "  `persons`.`numbers` = :numbers,\n" //
                        + "  `persons`.`lastname` = 'Schumacher'");

        assertThat(insert.build(SYBASE))
                .isEqualTo("INSERT INTO `persons` (`nickname`, `forename`, `birthday`, `deathday`, `lunch`, `lastUpdate`, `count`, `age`, `size`, `numbers`, `lastname`) VALUES (`forename`, 'Martin', NOW(), '2020-04-24', '14:46:00.000000', '2020-04-24 13:53:00.000000', 5, 38, 175.89, :numbers, 'Schumacher')");
        assertThat(insert.build(SYBASE, enabled())).isEqualTo("INSERT INTO `persons`\n" //
                + "  (`nickname`, `forename`, `birthday`, `deathday`, `lunch`, `lastUpdate`, `count`, `age`, `size`, `numbers`, `lastname`)\n" //
                + "VALUES\n" //
                + "  (`forename`, 'Martin', NOW(), '2020-04-24', '14:46:00.000000', '2020-04-24 13:53:00.000000', 5, 38, 175.89, :numbers, 'Schumacher')");

        assertThat(Insert.copy(insert).build(MYSQL)).isEqualTo(insert.build(MYSQL));
    }
}
