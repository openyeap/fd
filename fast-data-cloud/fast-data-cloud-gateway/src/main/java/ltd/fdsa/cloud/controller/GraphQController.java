//package ltd.fdsa.cloud.controller;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//import org.graphqlize.java.GraphQLResolver;
//import org.springframework.http.*;
//
//@RestController
//@Slf4j
//public class GraphQController {
//    private final GraphQLResolver graphQLResolver;
//
//    public GraphQController(GraphQLResolver graphQLResolver) {
//        this.graphQLResolver = graphQLResolver;
//    }
//
//    @PostMapping("/graphql")
//    public ResponseEntity handle(@RequestBody GraphQLRequest graphQLRequest) {
//        String result =
//                graphQLResolver.resolve(
//                        graphQLRequest.getQuery(),
//                        graphQLRequest.getVariables());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .body(result);
//    }
//
//    @Data
//    class GraphQLRequest {
//        private String query;
//        private Map<String, Object> variables;
//    }
//}