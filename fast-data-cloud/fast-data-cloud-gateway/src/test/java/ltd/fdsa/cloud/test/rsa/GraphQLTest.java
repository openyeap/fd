package ltd.fdsa.cloud.test.rsa;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.antlr.fql.GraphqlLexer;
import ltd.fdsa.antlr.fql.GraphqlParser;
import ltd.fdsa.cloud.service.GraphqlNewVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;


@Slf4j
public class GraphQLTest {

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
        query = "{\n" +
                "  hero : user(id:12) {\n" +
                "    name\n" +
                "    friends : friend(user_id:$user_id) {\n" +
                "      name\n" +
                "    }\n" +
                "  }\n" +
                "}";
        CharStream input = CharStreams.fromString(query);
        GraphqlLexer lexer = new GraphqlLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GraphqlParser parser = new GraphqlParser(tokens);
        var tree = parser.document();
        GraphqlNewVisitor visitor = new GraphqlNewVisitor();
        visitor.visit(tree);
    }

}
