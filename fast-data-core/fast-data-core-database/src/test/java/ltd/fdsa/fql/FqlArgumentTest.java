package ltd.fdsa.fql;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.fql.antlr.FqlLexer;
import ltd.fdsa.fql.antlr.FqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;


@Slf4j
public class FqlArgumentTest {

    @Test
    public void testIntArgument() {
        String query = "{\n" +
                "  hero : user(id_eq:12) {\n" +
                "    name\n" +
                "    friends : friend(user_id_eq:\"$user_id\") {\n" +
                "      name\n" +
                "    }\n" +
                "  }\n" +
                "}";
        CharStream input = CharStreams.fromString(query);
        test(input);
    }
    @Test
    public void testStringArgument() {
        String query = "{\n" +
                "  hero : user(id_eq:\"test\") {\n" +
                "    name\n" +
                "    friends : friend(user_id_eq:$ids) {\n" +
                "      name\n" +
                "    }\n" +
                "  }\n" +
                "}";
        CharStream input = CharStreams.fromString(query);
        test(input);
    }
    @Test
    public void testFloatArgument() {
        String query = "{\n" +
                "  hero : user(id_eq:112.342) {\n" +
                "    name\n" +
                "    friends : friend(user_id_eq:543.234) {\n" +
                "      name\n" +
                "    }\n" +
                "  }\n" +
                "}";
        CharStream input = CharStreams.fromString(query);
        test(input);
    }

    void test(CharStream input) {
        FqlLexer lexer = new FqlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FqlParser parser = new FqlParser(tokens);
        var tree = parser.document();
        JdbcFqlVisitor visitor = new JdbcFqlVisitor();
        visitor.visit(tree);
    }

}
