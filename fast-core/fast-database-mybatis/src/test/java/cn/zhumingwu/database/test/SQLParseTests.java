package cn.zhumingwu.database.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.database.sql.dialect.Dialects;
import cn.zhumingwu.database.sql.queries.Queries;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.sql.utils.Indentation;
import cn.zhumingwu.database.utils.PlaceHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = PlaceHolder.class)
public class SQLParseTests {


    String[] sqls = {"select a.name, b.phone from t_user a "
            + "inner join t_user_phone t_user_phone"
            + "on a.user_id = b.user_id "
            + "where a.name like 'test%' "
            + "group by a.name , b.phone  having count(a.name) > 0 "
            + "order by a.name , b.phone",
            "select name,count(test) from t_user group by name",
            "update t_password set password = 'test' from t_user where name like 'test%' " +
                    "and t_user.user_id= t_password.user_id",
            "insert into t_device (device_id,name,remark) values (2, 'test', 'remark')",
            "delete   from t_user  where user_id in (select user_id from t_user where t_password.user_id =t_user.user_id  and t_password.status is null)"};

    @Test
    public void TeatSQL() {
        // table 1
        var t_user = Table.create("t_user").as("t1");
        var user_id = t_user.intColumn("user_id").autoIncrement().build().as("id");
        var user_sort = t_user.column("sort").build();
        var user_status = t_user.column("status").build();
        var user_columns = t_user.columns("name", "user_name", "password", "age");
        // table 2
        var t_role = Table.create("t_role").as("t2");
        var role_id = t_role.intColumn("role_id").autoIncrement().build();
        var role_user_id = t_role.intColumn("user_id").build();
        var role_status = t_role.column("status").build();
        var role_columns = t_role.columns("name");

        var query = Queries.select(user_columns)
                .from(t_user)
                .join(t_role.on(user_id.eq(role_user_id)))
                .where(user_status.eq("1"))
                .orderDescendingBy(role_status)
                .orderBy(user_sort);
        log.info("================================================================\n{}\n================================================================",
                query.build(Dialects.POSTGRE, Indentation.enabled()));
    }

    @Test
    public void Test() {
        CustomSchemaStatVisitor visitor = new CustomSchemaStatVisitor();
        for (var sql : sqls) {
            //格式化输出
            String result = SQLUtils.format(sql, JdbcConstants.POSTGRESQL);
            for (var sqlStatement : SQLUtils.parseStatements(sql, JdbcConstants.POSTGRESQL)) {
                log.info("==========sql before:========\n{}\n\n=============================", sqlStatement);
                sqlStatement.accept(visitor);
                log.info("\n\n");
                log.info("==========sql after:========\n{}\n\n=============================", sqlStatement);
            }
        }
    }

}