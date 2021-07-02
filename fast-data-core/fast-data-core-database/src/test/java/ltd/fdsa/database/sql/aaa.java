package ltd.fdsa.database.sql;

import lombok.var;
import ltd.fdsa.database.sql.dialect.Dialects;
import ltd.fdsa.database.sql.queries.Queries;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.utils.Indentation;
import org.junit.jupiter.api.Test;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public class aaa {
    @Test
    public void test() {
        var t_user = Table.create("t_user").as("t1");

        var user_id = t_user.intColumn("user_id").size(32).build();
        var user_name = t_user.varCharColumn("name").size(64).build();
        var t_role = Table.create("t_role").as("t2");
        var role_id = t_role.intColumn("role_id").size(32).autoIncrement().build();
        var role_name = t_role.varCharColumn("name").build();
        var role_user_id = t_role.intColumn("user_id").build();

        var select = Queries.select(t_user.getColumns())
                .from(t_user)
                .join(t_role.on(user_id.eq(role_user_id)))
                .orderAscendingBy(user_id)
                .where(user_id.eq(1));
        select.print(Dialects.SYBASE, Indentation.enabled());
    }
}
