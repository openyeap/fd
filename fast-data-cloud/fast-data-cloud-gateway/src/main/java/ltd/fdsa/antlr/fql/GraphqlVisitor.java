// Generated from Graphql.g4 by ANTLR 4.9.3
package ltd.fdsa.antlr.fql;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GraphqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GraphqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#document}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocument(GraphqlParser.DocumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#selectionSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectionSet(GraphqlParser.SelectionSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#selection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelection(GraphqlParser.SelectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(GraphqlParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#enumValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumValue(GraphqlParser.EnumValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#arrayValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayValue(GraphqlParser.ArrayValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#arrayValueWithVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayValueWithVariable(GraphqlParser.ArrayValueWithVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#objectValueWithVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectValueWithVariable(GraphqlParser.ObjectValueWithVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#objectFieldWithVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectFieldWithVariable(GraphqlParser.ObjectFieldWithVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#directives}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectives(GraphqlParser.DirectivesContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirective(GraphqlParser.DirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(GraphqlParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(GraphqlParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#baseName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseName(GraphqlParser.BaseNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#enumValueName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumValueName(GraphqlParser.EnumValueNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(GraphqlParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(GraphqlParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#valueWithVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueWithVariable(GraphqlParser.ValueWithVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(GraphqlParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(GraphqlParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(GraphqlParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#listType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListType(GraphqlParser.ListTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GraphqlParser#nonNullType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonNullType(GraphqlParser.NonNullTypeContext ctx);
}