package cn.zhumingwu.database.fql;

import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.base.config.ProjectAutoConfiguration;
import cn.zhumingwu.database.fql.antlr.FqlLexer;
import cn.zhumingwu.database.fql.antlr.FqlParser;
import cn.zhumingwu.database.properties.JdbcApiProperties;
import cn.zhumingwu.database.service.JdbcApiService;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;


@Slf4j
@EnableAutoConfiguration(exclude = ProjectAutoConfiguration.class)
@SpringBootTest
@SpringBootConfiguration

public class FqlArgumentTest {
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcApiProperties properties;

    @Test
    public void testIntArgument() throws Exception {
        String query = "{\n" +
                "  user : t_user(user_id_eq:1) {\n" +
                "    name\n" +
                "    user_id\n" +
                "    roles : t_user_role(user_id_eq:$user_id) {\n" +
                "      role_id\n" +
                "      ... : t_role(role_id_eq: $role_id) {\n" +
                "         name\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        var input = CharStreams.fromString(query);
        test(input);
    }

    @Test
    public void testStringArgument() throws Exception {
        String query = "{\n" +
                "  hero : t_user(user_id_eq:\"1\") {\n" +
                "    name\n" +
                "    roles : t_user_role(user_id_eq:$user_id) {\n" +
                "      role_id\n" +
                "    }\n" +
                "  }\n" +
                "}";
        CharStream input = CharStreams.fromString(query);
        test(input);
    }

    @Test
    public void testFloatArgument() throws Exception {
        String query = "{\n" +
                "  user : t_user(user_id_eq:1.0) {\n" +
                "    name\n" +
                "    roles : t_user_role(user_id_eq:1) {\n" +
                "       ...\n" +
                "       role_id\n" +
                "    }\n" +
                "  }\n" +
                "}";
        CharStream input = CharStreams.fromString(query);
        test(input);
    }


    public void test(CharStream input) throws Exception {
        var lexer = new FqlLexer(input);
        var tokens = new CommonTokenStream(lexer);
        var parser = new FqlParser(tokens);
        var document = parser.document();
        var visitor = new JdbcFqlVisitor(new JdbcApiService(this.properties, this.dataSource));
        System.out.println(input);
        var data = visitor.visit(document);
        System.out.println(data.toString());
    }

}
