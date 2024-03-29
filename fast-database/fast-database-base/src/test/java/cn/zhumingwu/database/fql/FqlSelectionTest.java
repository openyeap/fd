package cn.zhumingwu.database.fql;

import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.database.fql.antlr.FqlLexer;
import cn.zhumingwu.database.fql.antlr.FqlParser;
import cn.zhumingwu.database.properties.JdbcApiProperties;
import cn.zhumingwu.database.service.JdbcApiService;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;


@Slf4j
@SpringBootTest

public class FqlSelectionTest {
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcApiProperties properties;
    @Test
    public void testSelectTwo() throws Exception {
        String query = "{\n" +
                "  ... : t_user(user_id_gt:0) {\n" +
                "    ...\n" +
                "    ...:t_user_role(role_id_gt:-1)\n" +
                "  }\n" +
                "}";
        CharStream input = CharStreams.fromString(query);
        doTest(input);
    }
    @Test
    public void testSelectInner() throws Exception {
        String query = "{\n" +
                "  user : t_user(user_id_eq:1) {\n" +
                "  name\n" +
                "  user_id\n" +
                "  roles : t_user_role(user_id_eq:$user_id) {\n" +
                "  role_id\n" +
                "  role_name : t_role(role_id_eq: $role_id) {\n" +
                "  name\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";
        CharStream input = CharStreams.fromString(query);
        doTest(input);
    }


    @Test
    public void testSelectThree() throws Exception {
        String query = "{\n" +
                "    user : t_user(user_id_eq:1) {\n" +
                "        ...\n" +
                "        roles:t_user_role(user_id_eq:$user_id) {\n" +
                "            ...\n" +
                "            role: t_role(role_id_eq:$role_id){" +
                "                 name }\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        CharStream input = CharStreams.fromString(query);
        doTest(input);
    }

    public void doTest(CharStream input) throws Exception {
        var lexer = new FqlLexer(input);
        var tokens = new CommonTokenStream(lexer);
        var parser = new FqlParser(tokens);
        var document = parser.document();
        var visitor = new JdbcFqlVisitor(new JdbcApiService(this.properties,this.dataSource));
        System.out.println(input);
        var data = visitor.visit(document);
        System.out.println(data.toString());
    }
}
