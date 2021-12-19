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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, BooleanValue=9, 
		NullValue=10, EXPAND=11, NAME=12, IntValue=13, FloatValue=14, StringValue=15, 
		Comment=16, LF=17, CR=18, LineTerminator=19, Space=20, Tab=21, Comma=22, 
		UnicodeBOM=23;
	public static final int
		RULE_document = 0, RULE_selectionSet = 1, RULE_selection = 2, RULE_alias = 3, 
		RULE_arrayValue = 4, RULE_arrayValueWithVariable = 5, RULE_arguments = 6, 
		RULE_argument = 7, RULE_name = 8, RULE_value = 9, RULE_valueWithVariable = 10, 
		RULE_variable = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"document", "selectionSet", "selection", "alias", "arrayValue", "arrayValueWithVariable", 
			"arguments", "argument", "name", "value", "valueWithVariable", "variable"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "':'", "'['", "']'", "'('", "')'", "'$'", null, "'null'", 
			"'...'", null, null, null, null, null, null, null, null, null, null, 
			"','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "BooleanValue", 
			"NullValue", "EXPAND", "NAME", "IntValue", "FloatValue", "StringValue", 
			"Comment", "LF", "CR", "LineTerminator", "Space", "Tab", "Comma", "UnicodeBOM"
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
			setState(24);
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
			setState(26);
			match(T__0);
			setState(28); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(27);
				selection();
				}
				}
				setState(30); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanValue) | (1L << NullValue) | (1L << EXPAND) | (1L << NAME))) != 0) );
			setState(32);
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
			setState(35);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(34);
				alias();
				}
				break;
			}
			setState(37);
			name();
			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(38);
				arguments();
				}
			}

			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(41);
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
			setState(44);
			name();
			setState(45);
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
		enterRule(_localctx, 8, RULE_arrayValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			match(T__3);
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << BooleanValue) | (1L << NullValue) | (1L << IntValue) | (1L << FloatValue) | (1L << StringValue))) != 0)) {
				{
				{
				setState(48);
				value();
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54);
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
		enterRule(_localctx, 10, RULE_arrayValueWithVariable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(T__3);
			setState(60);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__7) | (1L << BooleanValue) | (1L << NullValue) | (1L << IntValue) | (1L << FloatValue) | (1L << StringValue))) != 0)) {
				{
				{
				setState(57);
				valueWithVariable();
				}
				}
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(63);
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
		enterRule(_localctx, 12, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			match(T__5);
			setState(67); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(66);
				argument();
				}
				}
				setState(69); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanValue) | (1L << NullValue) | (1L << EXPAND) | (1L << NAME))) != 0) );
			setState(71);
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
		enterRule(_localctx, 14, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			name();
			setState(74);
			match(T__2);
			setState(75);
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

	public static class NameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(FqlParser.NAME, 0); }
		public TerminalNode BooleanValue() { return getToken(FqlParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(FqlParser.NullValue, 0); }
		public TerminalNode EXPAND() { return getToken(FqlParser.EXPAND, 0); }
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
		enterRule(_localctx, 16, RULE_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanValue) | (1L << NullValue) | (1L << EXPAND) | (1L << NAME))) != 0)) ) {
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

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode StringValue() { return getToken(FqlParser.StringValue, 0); }
		public TerminalNode IntValue() { return getToken(FqlParser.IntValue, 0); }
		public TerminalNode FloatValue() { return getToken(FqlParser.FloatValue, 0); }
		public TerminalNode BooleanValue() { return getToken(FqlParser.BooleanValue, 0); }
		public TerminalNode NullValue() { return getToken(FqlParser.NullValue, 0); }
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
		enterRule(_localctx, 18, RULE_value);
		try {
			setState(85);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case StringValue:
				enterOuterAlt(_localctx, 1);
				{
				setState(79);
				match(StringValue);
				}
				break;
			case IntValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(80);
				match(IntValue);
				}
				break;
			case FloatValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(81);
				match(FloatValue);
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 4);
				{
				setState(82);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 5);
				{
				setState(83);
				match(NullValue);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 6);
				{
				setState(84);
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
		enterRule(_localctx, 20, RULE_valueWithVariable);
		try {
			setState(94);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(87);
				variable();
				}
				break;
			case StringValue:
				enterOuterAlt(_localctx, 2);
				{
				setState(88);
				match(StringValue);
				}
				break;
			case IntValue:
				enterOuterAlt(_localctx, 3);
				{
				setState(89);
				match(IntValue);
				}
				break;
			case FloatValue:
				enterOuterAlt(_localctx, 4);
				{
				setState(90);
				match(FloatValue);
				}
				break;
			case BooleanValue:
				enterOuterAlt(_localctx, 5);
				{
				setState(91);
				match(BooleanValue);
				}
				break;
			case NullValue:
				enterOuterAlt(_localctx, 6);
				{
				setState(92);
				match(NullValue);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 7);
				{
				setState(93);
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
		enterRule(_localctx, 22, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			match(T__7);
			setState(97);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\31f\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\3\2\3\2\3\3\3\3\6\3\37\n\3\r\3\16\3 \3\3\3\3\3\4\5\4&\n"+
		"\4\3\4\3\4\5\4*\n\4\3\4\5\4-\n\4\3\5\3\5\3\5\3\6\3\6\7\6\64\n\6\f\6\16"+
		"\6\67\13\6\3\6\3\6\3\7\3\7\7\7=\n\7\f\7\16\7@\13\7\3\7\3\7\3\b\3\b\6\b"+
		"F\n\b\r\b\16\bG\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\5\13X\n\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\fa\n\f\3\r\3\r\3\r\3"+
		"\r\2\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\3\3\2\13\16\2k\2\32\3\2\2\2"+
		"\4\34\3\2\2\2\6%\3\2\2\2\b.\3\2\2\2\n\61\3\2\2\2\f:\3\2\2\2\16C\3\2\2"+
		"\2\20K\3\2\2\2\22O\3\2\2\2\24W\3\2\2\2\26`\3\2\2\2\30b\3\2\2\2\32\33\5"+
		"\4\3\2\33\3\3\2\2\2\34\36\7\3\2\2\35\37\5\6\4\2\36\35\3\2\2\2\37 \3\2"+
		"\2\2 \36\3\2\2\2 !\3\2\2\2!\"\3\2\2\2\"#\7\4\2\2#\5\3\2\2\2$&\5\b\5\2"+
		"%$\3\2\2\2%&\3\2\2\2&\'\3\2\2\2\')\5\22\n\2(*\5\16\b\2)(\3\2\2\2)*\3\2"+
		"\2\2*,\3\2\2\2+-\5\4\3\2,+\3\2\2\2,-\3\2\2\2-\7\3\2\2\2./\5\22\n\2/\60"+
		"\7\5\2\2\60\t\3\2\2\2\61\65\7\6\2\2\62\64\5\24\13\2\63\62\3\2\2\2\64\67"+
		"\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\67\65\3\2\2\289\7\7\2"+
		"\29\13\3\2\2\2:>\7\6\2\2;=\5\26\f\2<;\3\2\2\2=@\3\2\2\2><\3\2\2\2>?\3"+
		"\2\2\2?A\3\2\2\2@>\3\2\2\2AB\7\7\2\2B\r\3\2\2\2CE\7\b\2\2DF\5\20\t\2E"+
		"D\3\2\2\2FG\3\2\2\2GE\3\2\2\2GH\3\2\2\2HI\3\2\2\2IJ\7\t\2\2J\17\3\2\2"+
		"\2KL\5\22\n\2LM\7\5\2\2MN\5\26\f\2N\21\3\2\2\2OP\t\2\2\2P\23\3\2\2\2Q"+
		"X\7\21\2\2RX\7\17\2\2SX\7\20\2\2TX\7\13\2\2UX\7\f\2\2VX\5\n\6\2WQ\3\2"+
		"\2\2WR\3\2\2\2WS\3\2\2\2WT\3\2\2\2WU\3\2\2\2WV\3\2\2\2X\25\3\2\2\2Ya\5"+
		"\30\r\2Za\7\21\2\2[a\7\17\2\2\\a\7\20\2\2]a\7\13\2\2^a\7\f\2\2_a\5\f\7"+
		"\2`Y\3\2\2\2`Z\3\2\2\2`[\3\2\2\2`\\\3\2\2\2`]\3\2\2\2`^\3\2\2\2`_\3\2"+
		"\2\2a\27\3\2\2\2bc\7\n\2\2cd\5\22\n\2d\31\3\2\2\2\13 %),\65>GW`";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}