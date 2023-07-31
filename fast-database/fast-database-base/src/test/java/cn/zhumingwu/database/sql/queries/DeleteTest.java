package cn.zhumingwu.database.sql.queries;


import cn.zhumingwu.database.sql.columns.number.integer.IntColumn;
import cn.zhumingwu.database.sql.columns.string.VarCharColumn;
import cn.zhumingwu.database.sql.functions.Function;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static cn.zhumingwu.database.sql.dialect.Dialects.SYBASE;
import static cn.zhumingwu.database.sql.domain.LikeType.AFTER;
import static cn.zhumingwu.database.sql.queries.Delete.clearLimit;
import static cn.zhumingwu.database.sql.queries.Delete.clearWheres;
import static cn.zhumingwu.database.sql.queries.Queries.deleteFrom;
import static cn.zhumingwu.database.sql.utils.Indentation.enabled;
import static org.assertj.core.api.Assertions.assertThat;

class DeleteTest
{
    private static final Table PERSONS = Table.create("persons");

    private static final VarCharColumn LASTNAME = PERSONS.varCharColumn("lastname").build();
    private static final IntColumn AGE = PERSONS.intColumn("age").build();

    @Test
    void testBuildSimpleDelete()
    {
        assertThat(deleteFrom(PERSONS).where(LASTNAME.isEqualTo("Schumacher")).build(SYBASE))
                .isEqualTo("DELETE FROM `persons` WHERE `persons`.`lastname` = 'Schumacher'");
    }

    @Test
    void testBuildDelete()
    {
        var delete = deleteFrom(PERSONS)
                .where(LASTNAME.isNotLike("Nils", AFTER)
                        .and(Function.min(AGE).isGreaterThanOrEqualTo(50))
                        .and(LASTNAME.isEqualTo("Schumacher")
                                .or(LASTNAME.isEqualTo("Kuenzel"))))
                .limit(10);

        delete.println(MYSQL, enabled());
        delete.println(SYBASE, enabled());

        assertThat(delete.build(MYSQL))
                .isEqualTo("DELETE FROM `persons` WHERE (`persons`.`lastname` NOT LIKE 'Nils%' AND MIN(`persons`.`age`) >= 50 AND (`persons`.`lastname` = 'Schumacher' OR `persons`.`lastname` = 'Kuenzel')) LIMIT 10");

        assertThat(delete.build(MYSQL, enabled()))
                .isEqualTo("DELETE FROM\n" //
                        + "  `persons`\n" //
                        + "WHERE\n" //
                        + "(\n" //
                        + "  `persons`.`lastname` NOT LIKE 'Nils%'\n" //
                        + "  AND MIN(`persons`.`age`) >= 50\n" //
                        + "  AND\n" //
                        + "  (\n" //
                        + "    `persons`.`lastname` = 'Schumacher'\n" //
                        + "    OR `persons`.`lastname` = 'Kuenzel'\n" //
                        + "  )\n" //
                        + ")\n" //
                        + "LIMIT 10");

        assertThat(delete.build(SYBASE))
                .isEqualTo("DELETE TOP 10 FROM `persons` WHERE (`persons`.`lastname` NOT LIKE 'Nils%' AND MIN(`persons`.`age`) >= 50 AND (`persons`.`lastname` = 'Schumacher' OR `persons`.`lastname` = 'Kuenzel'))");

        assertThat(delete.build(SYBASE, enabled()))
                .isEqualTo("DELETE TOP 10 FROM\n" //
                        + "  `persons`\n" //
                        + "WHERE\n" //
                        + "(\n" //
                        + "  `persons`.`lastname` NOT LIKE 'Nils%'\n" //
                        + "  AND MIN(`persons`.`age`) >= 50\n" //
                        + "  AND\n" //
                        + "  (\n" //
                        + "    `persons`.`lastname` = 'Schumacher'\n" //
                        + "    OR `persons`.`lastname` = 'Kuenzel'\n" //
                        + "  )\n" //
                        + ")");

        assertThat(Delete.copy(delete).build(MYSQL)).isEqualTo(delete.build(MYSQL));
    }

    @Test
    void testBuildDeleteWithWhereNot()
    {
        var delete = deleteFrom(PERSONS).whereNot(LASTNAME.isLike("Nils", AFTER));

        delete.println(MYSQL, enabled());
        delete.println(SYBASE, enabled());

        assertThat(delete.build(MYSQL)).isEqualTo("DELETE FROM `persons` WHERE NOT `persons`.`lastname` LIKE 'Nils%'");

        assertThat(delete.build(MYSQL, enabled()))
                .isEqualTo("DELETE FROM\n" //
                        + "  `persons`\n" //
                        + "WHERE NOT `persons`.`lastname` LIKE 'Nils%'");

        assertThat(Delete.copy(delete).build(MYSQL)).isEqualTo(delete.build(MYSQL));
    }

    @Test
    void testClearWheres()
    {
        var delete = deleteFrom(PERSONS).where(LASTNAME.eq("Nils"));

        assertThat(delete.getWhere()).isNotNull();
        clearWheres(delete);
        assertThat(delete.getWhere()).isNull();
    }

    @Test
    void testClearLimit()
    {
        var delete = deleteFrom(PERSONS).limit(5);

        assertThat(delete.getLimitation()).isNotNull();
        clearLimit(delete);
        assertThat(delete.getLimitation()).isNull();
    }
}
