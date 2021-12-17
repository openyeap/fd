package ltd.fdsa.cloud.test.rsa;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.fql.JdbcFqlVisitor;
import ltd.fdsa.fql.antlr.FqlLexer;
import ltd.fdsa.fql.antlr.FqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;


@Slf4j
public class FqlTest {

    @Test
    public void testParser() {
        String query;
        query = "{\n" +
                "  hero : user(id:12) {\n" +
                "    name\n" +
                "    friends : friend(user_id:user_id) {\n" +
                "      name\n" +
                "    }\n" +
                "  }\n" +
                "}";
//        query = "{\n" +
//                "  hero : user(id:12) {\n" +
//                "    \n" +
//                "    friends : friend(user_id:user_id) {\n" +
//                "      name\n" +
//                "    }\n" +
//                "  }\n" +
//                "  login : user(user_name:admin,password:123456)\n" +
//                "  {\n" +
//                "      user_id\n" +
//                "      status\n" +
//                "  }\n" +
//                "}";
        CharStream input = CharStreams.fromString(query);
        FqlLexer lexer = new FqlLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FqlParser parser = new FqlParser(tokens);
        var tree = parser.document();
        JdbcFqlVisitor visitor = new JdbcFqlVisitor();
        visitor.visit(tree);
    }

}
