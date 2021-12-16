package ltd.fdsa.cloud.service;

import lombok.var;
import ltd.fdsa.antlr.fql.GraphqlBaseVisitor;
import ltd.fdsa.antlr.fql.GraphqlParser;

import java.util.HashMap;
import java.util.Map;

public class GraphqlNewVisitor extends GraphqlBaseVisitor<Map<String, Object>> {
    @Override
    public Map<String, Object> visitDocument(GraphqlParser.DocumentContext ctx) {
        return visitSelectionSet(ctx.selectionSet());
    }


    @Override
    public Map<String, Object> visitSelectionSet(GraphqlParser.SelectionSetContext ctx) {
        if (ctx == null || ctx.isEmpty()) {
            return null;
        }
        for (var selection : ctx.selection()) {
            visitSelection(selection);
        }
        return null;
    }

    @Override
    public Map<String, Object> visitSelection(GraphqlParser.SelectionContext ctx) {

        var alias = ctx.alias();
        var name = ctx.name();
        System.out.printf("%s.%s: ", alias == null ? "null" : alias.name().getText(), name == null ? "" : name.getText());
        var arguments = ctx.arguments();
        if (arguments != null && !arguments.isEmpty()) {
            System.out.printf(" %s,", visitArguments(ctx.arguments()));
        }
        System.out.println("");

        if (ctx.selectionSet() != null && !ctx.selectionSet().isEmpty()) {
            for (var selection : ctx.selectionSet().selection()) {
                visitSelection(selection);
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> visitArguments(GraphqlParser.ArgumentsContext ctx) {
        HashMap<String, Object> data = new HashMap<>();
        if (ctx.argument() != null && !ctx.argument().isEmpty()) {
            for (var argument : ctx.argument()) {
                var map = (Map<String, Object>) visitArgument(argument);
                data.putAll(map);
            }
        }
        return data;
    }

    @Override
    public Map<String, Object> visitArgument(GraphqlParser.ArgumentContext ctx) {
        var data = new HashMap<String, Object>();
        var name = ctx.name();
        var value = ctx.valueWithVariable();
        data.put(name.getText(), visitValueWithVariable(value).get("value"));
        return data;
    }

    @Override
    public Map<String, Object> visitValueWithVariable(GraphqlParser.ValueWithVariableContext ctx) {
        var data = new HashMap<String, Object>();
        data.put("value", ctx.getText());
        return data;
    }
}
