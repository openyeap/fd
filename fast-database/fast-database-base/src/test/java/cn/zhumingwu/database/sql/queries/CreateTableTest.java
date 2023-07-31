package cn.zhumingwu.database.sql.queries;


import cn.zhumingwu.database.sql.columns.datetime.DateColumn;
import cn.zhumingwu.database.sql.columns.datetime.DateTimeColumn;
import cn.zhumingwu.database.sql.columns.number.doubletype.DoubleColumn;
import cn.zhumingwu.database.sql.columns.number.integer.IntColumn;
import cn.zhumingwu.database.sql.columns.string.VarCharColumn;
import cn.zhumingwu.database.sql.schema.Schema;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static cn.zhumingwu.database.sql.dialect.Dialects.SYBASE;
import static cn.zhumingwu.database.sql.queries.Queries.createTable;
import static cn.zhumingwu.database.sql.utils.Indentation.enabled;
import static org.assertj.core.api.Assertions.assertThat;

class CreateTableTest
{
    public static final Schema DBA = Schema.create("dba");

    public static final Table PERSONS = DBA.table("persons");

    public static final VarCharColumn FORENAME = PERSONS.varCharColumn("forename").size(50).noDefault().build();
    public static final VarCharColumn LASTNAME = PERSONS.varCharColumn("lastname").size(50).defaultNull().build();
    public static final VarCharColumn NICKNAME = PERSONS.varCharColumn("nickname").size(30).defaultValue("Schubi").build();
    public static final IntColumn AGE = PERSONS.intColumn("age").size(5).unsigned().nullable(false).noDefault().autoIncrement().build();
    public static final DoubleColumn SIZE = PERSONS.doubleColumn("size").size(2.2).unsigned().nullable(false).defaultValue(55.8).build();
    public static final DateColumn BIRTHDAY = PERSONS.dateColumn("birthday").nullable(false).build();
    public static final DateTimeColumn HAPPENING = PERSONS.dateTimeColumn("happening").nullable(false).build();

    @Test
    void testCreateTable()
    {
        var createTable = createTable(PERSONS);
        createTable.println();
        createTable.println(SYBASE, enabled());

        assertThat(createTable.build(MYSQL))
                .isEqualTo("CREATE TABLE `dba`.`persons` (`forename` VARCHAR(50), `lastname` VARCHAR(50) DEFAULT NULL, `nickname` VARCHAR(30) DEFAULT 'Schubi', `age` INT(5) UNSIGNED NOT NULL AUTO_INCREMENT, `size` DOUBLE(2,2) UNSIGNED NOT NULL DEFAULT 55.8, `birthday` DATE NOT NULL, `happening` DATETIME NOT NULL, PRIMARY KEY (`age`))");

        assertThat(createTable.build(MYSQL, enabled()))
                .isEqualTo("CREATE TABLE `dba`.`persons`\n" //
                        + "(\n" //
                        + "  `forename` VARCHAR(50),\n" //
                        + "  `lastname` VARCHAR(50) DEFAULT NULL,\n" //
                        + "  `nickname` VARCHAR(30) DEFAULT 'Schubi',\n" //
                        + "  `age` INT(5) UNSIGNED NOT NULL AUTO_INCREMENT,\n" //
                        + "  `size` DOUBLE(2,2) UNSIGNED NOT NULL DEFAULT 55.8,\n" //
                        + "  `birthday` DATE NOT NULL,\n" //
                        + "  `happening` DATETIME NOT NULL,\n" //
                        + "  PRIMARY KEY (`age`)\n" //
                        + ")");

        assertThat(createTable.build(MYSQL)).isEqualTo(createTable.build(SYBASE));
        assertThat(createTable.build(MYSQL, enabled())).isEqualTo(createTable.build(SYBASE, enabled()));

        assertThat(CreateTable.copy(createTable).build(MYSQL)).isEqualTo(createTable.build(MYSQL));
    }
}
