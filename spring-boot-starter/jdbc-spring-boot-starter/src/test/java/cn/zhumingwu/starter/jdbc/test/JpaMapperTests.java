package cn.zhumingwu.starter.jdbc.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.starter.jdbc.mappers.TestMapper;
import cn.zhumingwu.starter.jdbc.sql.SingleSql;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
//@SpringBootTest
public class JpaMapperTests {


    @Test
    public void testQueryUserList() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Mike");
        data.put("age", 23);
        data.put("date", new Date());
        TestMapper<Entity> dm = TestMapper.build(Entity.class);
        Entity entity = dm.getEntity(data);
        System.out.println(entity);

    }

    @Test
    public void testUpdateUser() {
        String sql="";
        Object[] args= new Object[0];
        //增加
        var row = SingleSql.insert(test.class, new test(), new test());
        //查询
        var fdql = SingleSql.build(test.class).where("");
        //分页
        var list = fdql.pagedQuery();
        //删除
        row = fdql.delete(false);
        //更新
        fdql.update(new test());
        //执行
        var result = SingleSql.exec(sql, args, test.class);
    }

    @Data
    class test {
        String username;
        String password;
        Date createTime;
    }
}
