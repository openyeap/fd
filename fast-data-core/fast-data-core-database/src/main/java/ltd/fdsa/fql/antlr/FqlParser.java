// Generated from Fql.g4 by ANTLR 4.9.3
package ltd.fdsa.fql.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		BooleanValue=10, NullValue=11, FRAGMENT=12, QUERY=13, MUTATION=14, SUBSCRIPTION=15, 
		SCHEMA=16, SCALAR=17, TYPE=18, INTERFACE=19, IMPLEMENTS=20, ENUM=21, UNION=22, 
		INPUT=23, EXTEND=24, DIRECTIVE=25, ON_KEYWORD=26, REPEATABLE=27, NAME=28, 
		IntValue=29, FloatValue=30, StringValue=31, Comment=32, LF=33, CR=34, 
		LineTerminator=35, Space=36, Tab=37, Comma=38, UnicodeBOM=39;
	public static final int
		RULE_document = 0, RULE_selectionSet = 1, RULE_selection = 2, RULE_alias = 3, 
		RULE_enumValue = 4, RULE_arrayValue = 5, RULE_arrayValueWithVariable = 6, 
		RULE_arguments = 7, RULE_argument = 8, RULE_baseName = 9, RULE_enumValueName = 10, 
		RULE_name = 11, RULE_value = 12, RULE_valueWithVariable = 13, RULE_variable = 14, 
		RULE_type = 15, RULE_typeName = 16, RULE_listType = 17, RULE_nonNullType = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"document", "selectionSet", "selection", "alias", "enumValue", "arrayValue", 
			"arrayValueWithVariable", "arguments", "argument", "baseName", "enumValueName", 
			"name", "value", "valueWithVariable", "variable", "type", "typeName", 
			"listType", "nonNullType"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "':'", "'['", "']'", "'('", "')'", "'$'", "'!'", 
			null, "'null'", "'fragment'", "'query'", "'mutation'", "'subscription'", 
			"'schema'", "'scalar'", "'type'", "'interface'", "'implements'", "'enum'", 
			"'union'", "'input'", "'extend'", "'directive'", "'on'", "'repeatable'", 
			null, null, null, null, null, null, null, null, null, null, "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "BooleanValue", 
			"NullValue", "FRAGMENT", "QUERY", "MUTATION", "SUBSCRIPTION", "SCHEMA", 
			"SCALAR", "TYPE", "INTERFACE", "IMPLEMENTS", "ENUM", "UNION", "INPUT", 
			"EXTEND", "DIRECTIVE", "ON_KEYWORD", "REPEATABLE", "NAME", "IntValue", 
			"FloatValue", "StringValue", "Comment", "LF", "CR", "LineTerminator", 
			"Space", "Tab", "Comma", "UnicodeBOM"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Fql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class DocumentContext extends ParserRuleContext {
		public SelectionSetContext selectionSet() {
			return getRuleContext(SelectionSetContext.class,0);
		}
		public DocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_document; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentContext document() throws RecognitionException {
		DocumentContext _localctx = new DocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_document);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			selectionSet();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionSetContext extends ParserRuleContext {
		public List<SelectionContext> selection() {
			return getRuleContexts(SelectionContext.class);
		}
		public SelectionContext selection(int i) {
			return getRuleContext(SelectionContext.class,i);
		}
		public SelectionSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectionSet; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitSelectionSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionSetContext selectionSet() throws RecognitionException {
		SelectionSetContext _localctx = new SelectionSetContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_selectionSet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(T__0);
			setState(42); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(41);
				selection();
				}
				}
				setState(44); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanValue) | (1L << NullValue) | (1L << FRAGMENT) | (1L << QUERY) | (1L << MUTATION) | (1L << SUBSCRIPTION) | (1L << SCHEMA) | (1L << SCALAR) | (1L << TYPE) | (1L << INTERFACE) | (1L << IMPLEMENTS) | (1L << ENUM) | (1L << UNION) | (1L << INPUT) | (1L << EXTEND) | (1L << DIRECTIVE) | (1L << ON_KEYWORD) | (1L << REPEATABLE) | (1L << NAME))) != 0) );
			setState(46);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public SelectionSetContext selectionSet() {
			return getRuleContext(SelectionSetContext.class,0);
		}
		public SelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitSelection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionContext selection() throws RecognitionException {
		SelectionContext _localctx = new SelectionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_selection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(48);
				alias();
				}
				break;
			}
			setState(51);
			name();
			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(52);
				arguments();
				}
			}

			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(55);
				selectionSet();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			name();
			setState(59);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumValueContext extends ParserRuleContext {
		public EnumValueNameContext enumValueName() {
			return getRuleContext(EnumValueNameContext.class,0);
		}
		public EnumValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitEnumValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueContext enumValue() throws RecognitionException {
		EnumValueContext _localctx = new EnumValueContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_enumValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			enumValueName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayValueContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public ArrayValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayValue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitArrayValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayValueContext arrayValue() throws RecognitionException {
		ArrayValueContext _localctx = new ArrayValueContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_arrayValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(T__3);
			setState(67);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << BooleanValue) | (1L << NullValue) | (1L << FRAGMENT) | (1L << QUERY) | (1L << MUTATION) | (1L << SUBSCRIPTION) | (1L << SCHEMA) | (1L << SCALAR) | (1L << TYPE) | (1L << INTERFACE) | (1L << IMPLEMENTS) | (1L << ENUM) | (1L << UNION) | (1L << INPUT) | (1L << EXTEND) | (1L << DIRECTIVE) | (1L << ON_KEYWORD) | (1L << REPEATABLE) | (1L << NAME) | (1L << IntValue) | (1L << FloatValue) | (1L << StringValue))) != 0)) {
				{
				{
				setState(64);
				value();
				}
				}
				setState(69);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(70);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayValueWithVariableContext extends ParserRuleContext {
		public List<ValueWithVariableContext> valueWithVariable() {
			return getRuleContexts(ValueWithVariableContext.class);
		}
		public ValueWithVariableContext valueWithVariable(int i) {
			return getRuleContext(ValueWithVariableContext.class,i);
		}
		public ArrayValueWithVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayValueWithVariable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitArrayValueWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayValueWithVariableContext arrayValueWithVariable() throws RecognitionException {
		ArrayValueWithVariableContext _localctx = new ArrayValueWithVariableContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arrayValueWithVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(T__3);
			setState(76);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__7) | (1L << BooleanValue) | (1L << NullValue) | (1L << FRAGMENT) | (1L << QUERY) | (1L << MUTATION) | (1L << SUBSCRIPTION) | (1L << SCHEMA) | (1L << SCALAR) | (1L << TYPE) | (1L << INTERFACE) | (1L << IMPLEMENTS) | (1L << ENUM) | (1L << UNION) | (1L << INPUT) | (1L << EXTEND) | (1L << DIRECTIVE) | (1L << ON_KEYWORD) | (1L << REPEATABLE) | (1L << NAME) | (1L << IntValue) | (1L << FloatValue) | (1L << StringValue))) != 0)) {
				{
				{
				setState(73);
				valueWithVariable();
				}
				}
				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(79);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(T__5);
			setState(83); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(82);
				argument();
				}
				}
				setState(85); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanValue) | (1L << NullValue) | (1L << FRAGMENT) | (1L << QUERY) | (1L << MUTATION) | (1L << SUBSCRIPTION) | (1L << SCHEMA) | (1L << SCALAR) | (1L << TYPE) | (1L << INTERFACE) | (1L << IMPLEMENTS) | (1L << ENUM) | (1L << UNION) | (1L << INPUT) | (1L << EXTEND) | (1L << DIRECTIVE) | (1L << ON_KEYWORD) | (1L << REPEATABLE) | (1L << NAME))) != 0) );
			setState(87);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ValueWithVariableContext valueWithVariable() {
			return getRuleContext(ValueWithVariableContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			name();
			setState(90);
			match(T__2);
			setState(91);
			valueWithVariable();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BaseNameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(FqlParser.NAME, 0); }
		public TerminalNode FRAGMENT() { return getToken(FqlParser.FRAGMENT, 0); }
		public TerminalNode QUERY() { return getToken(FqlParser.QUERY, 0); }
		public TerminalNode MUTATION() { return getToken(FqlParser.MUTATION, 0); }
		public TerminalNode SUBSCRIPTION() { return getToken(FqlParser.SUBSCRIPTION, 0); }
		public TerminalNode SCHEMA() { return getToken(FqlParser.SCHEMA, 0); }
		public TerminalNode SCALAR() { return getToken(FqlParser.SCALAR, 0); }
		public TerminalNode TYPE() { return getToken(FqlParser.TYPE, 0); }
		public TerminalNode INTERFACE() { return getToken(FqlParser.INTERFACE, 0); }
		public TerminalNode IMPLEMENTS() { return getToken(FqlParser.IMPLEMENTS, 0); }
		public TerminalNode ENUM() { return getToken(FqlParser.ENUM, 0); }
		public TerminalNode UNION() { return getToken(FqlParser.UNION, 0); }
		public TerminalNode INPUT() { return getToken(FqlParser.INPUT, 0); }
		public TerminalNode EXTEND() { return getToken(FqlParser.EXTEND, 0); }
		public TerminalNode DIRECTIVE() { return getToken(FqlParser.DIRECTIVE, 0); }
		public TerminalNode REPEATABLE() { return getToken(FqlParser.REPEATABLE, 0); }
		public BaseNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitBaseName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseNameContext baseName() throws RecognitionException {
		BaseNameContext _localctx = new BaseNameContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_baseName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FRAGMENT) | (1L << QUERY) | (1L << MUTATION) | (1L << SUBSCRIPTION) | (1L << SCHEMA) | (1L << SCALAR) | (1L << TYPE) | (1L << INTERFACE) | (1L << IMPLEMENTS) | (1L << ENUM) | (1L << UNION) | (1L << INPUT) | (1L << EXTEND) | (1L << DIRECTIVE) | (1L << REPEATABLE) | (1L << NAME))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumValueNameContext extends ParserRuleContext {
		public BaseNameContext baseName() {
			return getRuleContext(BaseNameContext.class,0);
		}
		public TerminalNode ON_KEYWORD() { return getToken(FqlParser.ON_KEYWORD, 0); }
		public EnumValueNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumValueName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitEnumValueName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumValueNameContext enumValueName() throws RecognitionException {
		EnumValueNameContext _localctx = new EnumValueNameContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_enumValueName);
		try {
			setState(97);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(95);
				baseName();
				}
				break;
			case ON_KEYWORD:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
				match(ON_KEYWORD);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NameContext extends ParserRuleContext {
		public BaseNameContext baseName() {
			return getRuleContext(BaseNameContext.class,0);
		}
		public TerminalNode BooleanValue() { return getToken(FqlParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(FqlParser.NullValue, 0); }
		public TerminalNode ON_KEYWORD() { return getToken(FqlParser.ON_KEYWORD, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_name);
		try {
			setState(103);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(99);
				baseName();
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				match(NullValue);
				}
				break;
			case ON_KEYWORD:
				enterOuterAlt(_localctx, 4);
				{
				setState(102);
				match(ON_KEYWORD);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode StringValue() { return getToken(FqlParser.StringValue, 0); }
		public TerminalNode IntValue() { return getToken(FqlParser.IntValue, 0); }
		public TerminalNode FloatValue() { return getToken(FqlParser.FloatValue, 0); }
		public TerminalNode BooleanValue() { return getToken(FqlParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(FqlParser.NullValue, 0); }
		public EnumValueContext enumValue() {
			return getRuleContext(EnumValueContext.class,0);
		}
		public ArrayValueContext arrayValue() {
			return getRuleContext(ArrayValueContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_value);
		try {
			setState(112);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case StringValue:
				enterOuterAlt(_localctx, 1);
				{
				setState(105);
				match(StringValue);
				}
				break;
			case IntValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(106);
				match(IntValue);
				}
				break;
			case FloatValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(107);
				match(FloatValue);
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 4);
				{
				setState(108);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 5);
				{
				setState(109);
				match(NullValue);
				}
				break;
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case ON_KEYWORD:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 6);
				{
				setState(110);
				enumValue();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 7);
				{
				setState(111);
				arrayValue();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueWithVariableContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode StringValue() { return getToken(FqlParser.StringValue, 0); }
		public TerminalNode IntValue() { return getToken(FqlParser.IntValue, 0); }
		public TerminalNode FloatValue() { return getToken(FqlParser.FloatValue, 0); }
		public TerminalNode BooleanValue() { return getToken(FqlParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(FqlParser.NullValue, 0); }
		public EnumValueContext enumValue() {
			return getRuleContext(EnumValueContext.class,0);
		}
		public ArrayValueWithVariableContext arrayValueWithVariable() {
			return getRuleContext(ArrayValueWithVariableContext.class,0);
		}
		public ValueWithVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueWithVariable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitValueWithVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueWithVariableContext valueWithVariable() throws RecognitionException {
		ValueWithVariableContext _localctx = new ValueWithVariableContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_valueWithVariable);
		try {
			setState(122);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(114);
				variable();
				}
				break;
			case StringValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(115);
				match(StringValue);
				}
				break;
			case IntValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(116);
				match(IntValue);
				}
				break;
			case FloatValue:
				enterOuterAlt(_localctx, 4);
				{
				setState(117);
				match(FloatValue);
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 5);
				{
				setState(118);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 6);
				{
				setState(119);
				match(NullValue);
				}
				break;
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case ON_KEYWORD:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 7);
				{
				setState(120);
				enumValue();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 8);
				{
				setState(121);
				arrayValueWithVariable();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			match(T__7);
			setState(125);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ListTypeContext listType() {
			return getRuleContext(ListTypeContext.class,0);
		}
		public NonNullTypeContext nonNullType() {
			return getRuleContext(NonNullTypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_type);
		try {
			setState(130);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(127);
				typeName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(128);
				listType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(129);
				nonNullType();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeNameContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_typeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListTypeContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ListTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitListType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListTypeContext listType() throws RecognitionException {
		ListTypeContext _localctx = new ListTypeContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_listType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			match(T__3);
			setState(135);
			type();
			setState(136);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonNullTypeContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ListTypeContext listType() {
			return getRuleContext(ListTypeContext.class,0);
		}
		public NonNullTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonNullType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FqlVisitor ) return ((FqlVisitor<? extends T>)visitor).visitNonNullType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonNullTypeContext nonNullType() throws RecognitionException {
		NonNullTypeContext _localctx = new NonNullTypeContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_nonNullType);
		try {
			setState(144);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BooleanValue:
			case NullValue:
			case FRAGMENT:
			case QUERY:
			case MUTATION:
			case SUBSCRIPTION:
			case SCHEMA:
			case SCALAR:
			case TYPE:
			case INTERFACE:
			case IMPLEMENTS:
			case ENUM:
			case UNION:
			case INPUT:
			case EXTEND:
			case DIRECTIVE:
			case ON_KEYWORD:
			case REPEATABLE:
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(138);
				typeName();
				setState(139);
				match(T__8);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(141);
				listType();
				setState(142);
				match(T__8);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3)\u0095\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\3\2\3\3\3\3\6\3-\n\3\r\3\16\3.\3\3\3\3\3\4\5"+
		"\4\64\n\4\3\4\3\4\5\48\n\4\3\4\5\4;\n\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\7"+
		"\7D\n\7\f\7\16\7G\13\7\3\7\3\7\3\b\3\b\7\bM\n\b\f\b\16\bP\13\b\3\b\3\b"+
		"\3\t\3\t\6\tV\n\t\r\t\16\tW\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f"+
		"\5\fd\n\f\3\r\3\r\3\r\3\r\5\rj\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\5\16s\n\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17}\n\17\3\20\3"+
		"\20\3\20\3\21\3\21\3\21\5\21\u0085\n\21\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u0093\n\24\3\24\2\2\25\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&\2\3\4\2\16\33\35\36\2\u009c\2(\3\2\2"+
		"\2\4*\3\2\2\2\6\63\3\2\2\2\b<\3\2\2\2\n?\3\2\2\2\fA\3\2\2\2\16J\3\2\2"+
		"\2\20S\3\2\2\2\22[\3\2\2\2\24_\3\2\2\2\26c\3\2\2\2\30i\3\2\2\2\32r\3\2"+
		"\2\2\34|\3\2\2\2\36~\3\2\2\2 \u0084\3\2\2\2\"\u0086\3\2\2\2$\u0088\3\2"+
		"\2\2&\u0092\3\2\2\2()\5\4\3\2)\3\3\2\2\2*,\7\3\2\2+-\5\6\4\2,+\3\2\2\2"+
		"-.\3\2\2\2.,\3\2\2\2./\3\2\2\2/\60\3\2\2\2\60\61\7\4\2\2\61\5\3\2\2\2"+
		"\62\64\5\b\5\2\63\62\3\2\2\2\63\64\3\2\2\2\64\65\3\2\2\2\65\67\5\30\r"+
		"\2\668\5\20\t\2\67\66\3\2\2\2\678\3\2\2\28:\3\2\2\29;\5\4\3\2:9\3\2\2"+
		"\2:;\3\2\2\2;\7\3\2\2\2<=\5\30\r\2=>\7\5\2\2>\t\3\2\2\2?@\5\26\f\2@\13"+
		"\3\2\2\2AE\7\6\2\2BD\5\32\16\2CB\3\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2"+
		"FH\3\2\2\2GE\3\2\2\2HI\7\7\2\2I\r\3\2\2\2JN\7\6\2\2KM\5\34\17\2LK\3\2"+
		"\2\2MP\3\2\2\2NL\3\2\2\2NO\3\2\2\2OQ\3\2\2\2PN\3\2\2\2QR\7\7\2\2R\17\3"+
		"\2\2\2SU\7\b\2\2TV\5\22\n\2UT\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3\2\2\2XY"+
		"\3\2\2\2YZ\7\t\2\2Z\21\3\2\2\2[\\\5\30\r\2\\]\7\5\2\2]^\5\34\17\2^\23"+
		"\3\2\2\2_`\t\2\2\2`\25\3\2\2\2ad\5\24\13\2bd\7\34\2\2ca\3\2\2\2cb\3\2"+
		"\2\2d\27\3\2\2\2ej\5\24\13\2fj\7\f\2\2gj\7\r\2\2hj\7\34\2\2ie\3\2\2\2"+
		"if\3\2\2\2ig\3\2\2\2ih\3\2\2\2j\31\3\2\2\2ks\7!\2\2ls\7\37\2\2ms\7 \2"+
		"\2ns\7\f\2\2os\7\r\2\2ps\5\n\6\2qs\5\f\7\2rk\3\2\2\2rl\3\2\2\2rm\3\2\2"+
		"\2rn\3\2\2\2ro\3\2\2\2rp\3\2\2\2rq\3\2\2\2s\33\3\2\2\2t}\5\36\20\2u}\7"+
		"!\2\2v}\7\37\2\2w}\7 \2\2x}\7\f\2\2y}\7\r\2\2z}\5\n\6\2{}\5\16\b\2|t\3"+
		"\2\2\2|u\3\2\2\2|v\3\2\2\2|w\3\2\2\2|x\3\2\2\2|y\3\2\2\2|z\3\2\2\2|{\3"+
		"\2\2\2}\35\3\2\2\2~\177\7\n\2\2\177\u0080\5\30\r\2\u0080\37\3\2\2\2\u0081"+
		"\u0085\5\"\22\2\u0082\u0085\5$\23\2\u0083\u0085\5&\24\2\u0084\u0081\3"+
		"\2\2\2\u0084\u0082\3\2\2\2\u0084\u0083\3\2\2\2\u0085!\3\2\2\2\u0086\u0087"+
		"\5\30\r\2\u0087#\3\2\2\2\u0088\u0089\7\6\2\2\u0089\u008a\5 \21\2\u008a"+
		"\u008b\7\7\2\2\u008b%\3\2\2\2\u008c\u008d\5\"\22\2\u008d\u008e\7\13\2"+
		"\2\u008e\u0093\3\2\2\2\u008f\u0090\5$\23\2\u0090\u0091\7\13\2\2\u0091"+
		"\u0093\3\2\2\2\u0092\u008c\3\2\2\2\u0092\u008f\3\2\2\2\u0093\'\3\2\2\2"+
		"\17.\63\67:ENWcir|\u0084\u0092";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}