package cn.zhumingwu.database.sql;

import cn.zhumingwu.database.sql.functions.Function;
import lombok.var;
import cn.zhumingwu.database.sql.dialect.Dialects;
import cn.zhumingwu.database.sql.queries.Queries;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.sql.utils.Indentation;
import org.junit.jupiter.api.Test;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public class SqlBuilderTest {
    @Test
    public void testSelect() {
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

    @Test
    public void testSelectCount() {
        var t_user = Table.create("t_user");
        var id = t_user.bigIntColumn("id");
        var select = Queries.select(Function.distinct(id.build()).as("cnt"))
                .from(t_user);

        System.out.println(select.build(Dialects.MYSQL, Indentation.enabled()));
    }
}
