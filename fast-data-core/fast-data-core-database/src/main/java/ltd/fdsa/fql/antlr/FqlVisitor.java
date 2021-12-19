// Generated from Fql.g4 by ANTLR 4.9.3
package ltd.fdsa.fql.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FqlParser#document}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocument(FqlParser.DocumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#selectionSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectionSet(FqlParser.SelectionSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#selection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelection(FqlParser.SelectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(FqlParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#enumValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumValue(FqlParser.EnumValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#arrayValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayValue(FqlParser.ArrayValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#arrayValueWithVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayValueWithVariable(FqlParser.ArrayValueWithVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(FqlParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(FqlParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#baseName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseName(FqlParser.BaseNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#enumValueName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumValueName(FqlParser.EnumValueNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(FqlParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(FqlParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#valueWithVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueWithVariable(FqlParser.ValueWithVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(FqlParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(FqlParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(FqlParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#listType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListType(FqlParser.ListTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FqlParser#nonNullType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonNullType(FqlParser.NonNullTypeContext ctx);
}