package ltd.fdsa.database.sql.queries;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.queries.Queries.select;
import static ltd.fdsa.database.sql.schema.Table.create;
import static ltd.fdsa.database.sql.utils.Indentation.enabled;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createStrictMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.var;
import org.junit.jupiter.api.Test;

class QueryTest {
    @Test
    void testPrintAndPrintln() {
        var query = select().from(create("table"));

        assertThat(query).isNotNull();

        query.print();
        System.out.println();
        query.println();
        query.print(MYSQL);
        System.out.println();
        query.println(MYSQL);
        query.print("MYSQL");
        System.out.println();
        query.println("MYSQL");
        query.print(enabled());
        System.out.println();
        query.println(enabled());
        query.print(MYSQL, enabled());
        System.out.println();
        query.println(MYSQL, enabled());
        query.print("MYSQL", enabled());
        System.out.println();
        query.println("MYSQL", enabled());

        query.print(System.out);
        System.out.println();
        query.println(System.out);
        query.print(System.out, MYSQL);
        System.out.println();
        query.println(System.out, MYSQL);
        query.print(System.out, "MYSQL");
        System.out.println();
        query.println(System.out, "MYSQL");
        query.print(System.out, enabled());
        System.out.println();
        query.println(System.out, enabled());
        query.print(System.out, MYSQL, enabled());
        System.out.println();
        query.println(System.out, MYSQL, enabled());
        query.print(System.out, "MYSQL", enabled());
        System.out.println();
        query.println(System.out, "MYSQL", enabled());
    }

    @Test
    void testPrepare() throws SQLException {
        var query = select().from(create("table"));

        try (var connection = createStrictMock(Connection.class)) {
            expect(connection.prepareStatement("SELECT * FROM `table`"))
                    .andReturn(createStrictMock(PreparedStatement.class));
            connection.close();

            replayAll();
            assertThat(query.prepare(connection)).isInstanceOf(PreparedStatement.class);
        }

        verifyAll();
    }

    @Test
    void testPrepareWithDialect() throws SQLException {
        var query = select().from(create("table"));

        try (var connection = createStrictMock(Connection.class)) {
            expect(connection.prepareStatement("SELECT * FROM `table`"))
                    .andReturn(createStrictMock(PreparedStatement.class));
            connection.close();

            replayAll();
            assertThat(query.prepare(connection, MYSQL)).isInstanceOf(PreparedStatement.class);
        }

        verifyAll();
    }

    @Test
    void testPrepareWithDialectName() throws SQLException {
        var query = select().from(create("table"));

        try (var connection = createStrictMock(Connection.class)) {
            expect(connection.prepareStatement("SELECT * FROM `table`"))
                    .andReturn(createStrictMock(PreparedStatement.class));
            connection.close();

            replayAll();
            assertThat(query.prepare(connection, "MYSQL")).isInstanceOf(PreparedStatement.class);
        }

        verifyAll();
    }
}
