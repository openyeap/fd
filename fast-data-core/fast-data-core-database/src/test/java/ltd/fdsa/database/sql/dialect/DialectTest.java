package ltd.fdsa.database.sql.dialect;

import static ltd.fdsa.database.sql.dialect.Dialects.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.domain.exceptions.UnknownDialectException;

class DialectTest
{
    @Test
    void testForNameRegisterAndUnregister()
    {
        assertThat(Dialect.forName("MYSQL")).isSameAs(MYSQL);
        assertThat(Dialect.forName("MsSQL")).isSameAs(MSSQL);
        assertThat(Dialect.forName("Postgre")).isSameAs(POSTGRE);
        assertThat(Dialect.forName("SYBASE")).isSameAs(SYBASE);

        Dialect.unregister("MYSQL");

        assertThatThrownBy(() -> Dialect.forName("MYSQL")).isInstanceOf(UnknownDialectException.class)
                .hasMessage("no dialect registered for name 'MYSQL'");
        assertThat(Dialect.forName("SYBASE")).isSameAs(SYBASE);

        Dialect.register(MYSQL);

        assertThat(Dialect.forName("MYSQL")).isSameAs(MYSQL);
        assertThat(Dialect.forName("SYBASE")).isSameAs(SYBASE);
    }
}
