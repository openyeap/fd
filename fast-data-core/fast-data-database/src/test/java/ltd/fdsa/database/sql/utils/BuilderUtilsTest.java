package ltd.fdsa.database.sql.utils;

import lombok.var;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.domain.Plain;
import ltd.fdsa.database.sql.functions.Function;
import ltd.fdsa.database.sql.schema.Schema;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.domain.Placeholder.placeholder;
import static ltd.fdsa.database.sql.utils.BuilderUtils.getValued;
import static ltd.fdsa.database.sql.utils.Indentation.disabled;
import static org.assertj.core.api.Assertions.assertThat;

class BuilderUtilsTest
{
    @Test
    void testGetValued()
    {
        var context = new BuildingContext(MYSQL, "");
        var forename = Schema.create("dba").table("persons").varCharColumn("forename").size(50).noDefault().build();

        assertThat(getValued(null, context, disabled())).isEqualTo("NULL");
        assertThat(getValued(forename, context, disabled())).isEqualTo("`dba`.`persons`.`forename`");
        assertThat(getValued(Function.now(), context, disabled())).isEqualTo("NOW()");
        assertThat(getValued(new Plain("plain"), context, disabled())).isEqualTo("plain");
        assertThat(getValued(placeholder(), context, disabled())).isEqualTo("?");
        assertThat(getValued(Integer.valueOf(8), context, disabled())).isEqualTo("8");
        assertThat(getValued(Long.valueOf(8), context, disabled())).isEqualTo("8");
        assertThat(getValued(Double.valueOf(55.8), context, disabled())).isEqualTo("55.8");
        assertThat(getValued(Float.valueOf(55.8f), context, disabled())).isEqualTo("55.8");
        assertThat(getValued(LocalDate.of(2019, 12, 27), context, disabled())).isEqualTo("'2019-12-27'");
        assertThat(getValued(LocalDateTime.of(2019, 12, 27, 19, 38, 5, 234567), context, disabled())).isEqualTo("'2019-12-27 19:38:05.234567'");
        assertThat(getValued(LocalTime.of(19, 38, 5, 234567), context, disabled())).isEqualTo("'19:38:05.234567'");
        assertThat(getValued("anyValue", context, disabled())).isEqualTo("'anyValue'");
    }
}
