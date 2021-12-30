// Generated from Fql.g4 by ANTLR 4.9.3
package ltd.fdsa.database.fql.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FqlLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, BooleanValue=9, 
		NullValue=10, EXPAND=11, NAME=12, IntValue=13, FloatValue=14, StringValue=15, 
		Comment=16, LF=17, CR=18, LineTerminator=19, Space=20, Tab=21, Comma=22, 
		UnicodeBOM=23;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "BooleanValue", 
			"NullValue", "EXPAND", "NAME", "IntValue", "IntegerPart", "NegativeSign", 
			"NonZeroDigit", "FloatValue", "FractionalPart", "ExponentPart", "ExponentIndicator", 
			"Sign", "Digit", "StringValue", "BlockStringCharacter", "StringCharacter", 
			"EscapedCharacter", "EscapedUnicode", "Hex", "SourceCharacter", "SourceCharacterWithoutLineFeed", 
			"Comment", "LF", "CR", "LineTerminator", "Space", "Tab", "Comma", "UnicodeBOM"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "'('", "')'", "':'", "'['", "']'", "'$'", null, "'null'", 
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

	    public boolean isDigit(int c) {        return c >= '0' && c <= '9';    }    public boolean isNameStart(int c) {        return '_' == c ||          (c >= 'A' && c <= 'Z') ||          (c >= 'a' && c <= 'z');    }    public boolean isDot(int c) {        return '.' == c;    }

	public FqlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Fql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return IntValue_sempred((RuleContext)_localctx, predIndex);
		case 16:
			return FloatValue_sempred((RuleContext)_localctx, predIndex);
		case 22:
			return StringValue_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean IntValue_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return  !isDigit(_input.LA(1)) && !isDot(_input.LA(1)) && !isNameStart(_input.LA(1))  ;
		}
		return true;
	}
	private boolean FloatValue_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return  !isDigit(_input.LA(1)) && !isDot(_input.LA(1)) && !isNameStart(_input.LA(1))  ;
		case 2:
			return  !isDigit(_input.LA(1)) && !isDot(_input.LA(1)) && !isNameStart(_input.LA(1))  ;
		case 3:
			return  !isDigit(_input.LA(1)) && !isDot(_input.LA(1)) && !isNameStart(_input.LA(1))  ;
		}
		return true;
	}
	private boolean StringValue_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return  _input.LA(1) != '"';
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\31\u0123\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\3\3\3\3\4\3"+
		"\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\5\ni\n\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\7\r"+
		"v\n\r\f\r\16\ry\13\r\3\16\3\16\3\16\3\17\5\17\177\n\17\3\17\3\17\5\17"+
		"\u0083\n\17\3\17\3\17\7\17\u0087\n\17\f\17\16\17\u008a\13\17\5\17\u008c"+
		"\n\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00a2\n\22\3\23\3\23\6\23\u00a6"+
		"\n\23\r\23\16\23\u00a7\3\24\3\24\5\24\u00ac\n\24\3\24\6\24\u00af\n\24"+
		"\r\24\16\24\u00b0\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\30\3"+
		"\30\3\30\6\30\u00bf\n\30\r\30\16\30\u00c0\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\7\30\u00ca\n\30\f\30\16\30\u00cd\13\30\3\30\3\30\3\30\5\30\u00d2"+
		"\n\30\3\31\3\31\3\31\3\31\3\31\5\31\u00d9\n\31\3\32\5\32\u00dc\n\32\3"+
		"\32\3\32\3\32\3\32\3\32\3\32\5\32\u00e4\n\32\3\33\3\33\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\6\34\u00ef\n\34\r\34\16\34\u00f0\3\34\3\34\5\34\u00f5"+
		"\n\34\3\35\3\35\3\36\5\36\u00fa\n\36\3\37\5\37\u00fd\n\37\3 \3 \7 \u0101"+
		"\n \f \16 \u0104\13 \3 \3 \3!\3!\3!\3!\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$"+
		"\3$\3$\3$\3%\3%\3%\3%\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\u00cb\2(\3\3\5\4\7"+
		"\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\2\37\2!\2#\20"+
		"%\2\'\2)\2+\2-\2/\21\61\2\63\2\65\2\67\29\2;\2=\2?\22A\23C\24E\25G\26"+
		"I\27K\30M\31\3\2\16\5\2C\\aac|\6\2\62;C\\aac|\4\2GGgg\4\2--//\n\2$$\61"+
		"\61^^ddhhppttvv\5\2\62;CHch\3\2\f\f\3\2\17\17\3\2\u202a\u202b\3\2\"\""+
		"\3\2\13\13\3\2\uff01\uff01\5\b\2\2\2\13\2\r\2\16\2\20\2#\2%\2]\2_\2\ud801"+
		"\2\ue002\2\1\22\4\2\2\2\ud801\2\ue002\2\1\22\6\2\2\2\13\2\r\2\16\2\20"+
		"\2\ud801\2\ue002\2\1\22\u0128\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2#\3\2\2\2\2/\3"+
		"\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2"+
		"\2\2K\3\2\2\2\2M\3\2\2\2\3O\3\2\2\2\5Q\3\2\2\2\7S\3\2\2\2\tU\3\2\2\2\13"+
		"W\3\2\2\2\rY\3\2\2\2\17[\3\2\2\2\21]\3\2\2\2\23h\3\2\2\2\25j\3\2\2\2\27"+
		"o\3\2\2\2\31s\3\2\2\2\33z\3\2\2\2\35\u008b\3\2\2\2\37\u008d\3\2\2\2!\u008f"+
		"\3\2\2\2#\u00a1\3\2\2\2%\u00a3\3\2\2\2\'\u00a9\3\2\2\2)\u00b2\3\2\2\2"+
		"+\u00b4\3\2\2\2-\u00b6\3\2\2\2/\u00d1\3\2\2\2\61\u00d8\3\2\2\2\63\u00e3"+
		"\3\2\2\2\65\u00e5\3\2\2\2\67\u00f4\3\2\2\29\u00f6\3\2\2\2;\u00f9\3\2\2"+
		"\2=\u00fc\3\2\2\2?\u00fe\3\2\2\2A\u0107\3\2\2\2C\u010b\3\2\2\2E\u010f"+
		"\3\2\2\2G\u0113\3\2\2\2I\u0117\3\2\2\2K\u011b\3\2\2\2M\u011f\3\2\2\2O"+
		"P\7}\2\2P\4\3\2\2\2QR\7\177\2\2R\6\3\2\2\2ST\7*\2\2T\b\3\2\2\2UV\7+\2"+
		"\2V\n\3\2\2\2WX\7<\2\2X\f\3\2\2\2YZ\7]\2\2Z\16\3\2\2\2[\\\7_\2\2\\\20"+
		"\3\2\2\2]^\7&\2\2^\22\3\2\2\2_`\7v\2\2`a\7t\2\2ab\7w\2\2bi\7g\2\2cd\7"+
		"h\2\2de\7c\2\2ef\7n\2\2fg\7u\2\2gi\7g\2\2h_\3\2\2\2hc\3\2\2\2i\24\3\2"+
		"\2\2jk\7p\2\2kl\7w\2\2lm\7n\2\2mn\7n\2\2n\26\3\2\2\2op\7\60\2\2pq\7\60"+
		"\2\2qr\7\60\2\2r\30\3\2\2\2sw\t\2\2\2tv\t\3\2\2ut\3\2\2\2vy\3\2\2\2wu"+
		"\3\2\2\2wx\3\2\2\2x\32\3\2\2\2yw\3\2\2\2z{\5\35\17\2{|\6\16\2\2|\34\3"+
		"\2\2\2}\177\5\37\20\2~}\3\2\2\2~\177\3\2\2\2\177\u0080\3\2\2\2\u0080\u008c"+
		"\7\62\2\2\u0081\u0083\5\37\20\2\u0082\u0081\3\2\2\2\u0082\u0083\3\2\2"+
		"\2\u0083\u0084\3\2\2\2\u0084\u0088\5!\21\2\u0085\u0087\5-\27\2\u0086\u0085"+
		"\3\2\2\2\u0087\u008a\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089"+
		"\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008b~\3\2\2\2\u008b\u0082\3\2\2\2"+
		"\u008c\36\3\2\2\2\u008d\u008e\7/\2\2\u008e \3\2\2\2\u008f\u0090\4\63;"+
		"\2\u0090\"\3\2\2\2\u0091\u0092\5\35\17\2\u0092\u0093\5%\23\2\u0093\u0094"+
		"\5\'\24\2\u0094\u0095\3\2\2\2\u0095\u0096\6\22\3\2\u0096\u00a2\3\2\2\2"+
		"\u0097\u0098\5\35\17\2\u0098\u0099\5%\23\2\u0099\u009a\3\2\2\2\u009a\u009b"+
		"\6\22\4\2\u009b\u00a2\3\2\2\2\u009c\u009d\5\35\17\2\u009d\u009e\5\'\24"+
		"\2\u009e\u009f\3\2\2\2\u009f\u00a0\6\22\5\2\u00a0\u00a2\3\2\2\2\u00a1"+
		"\u0091\3\2\2\2\u00a1\u0097\3\2\2\2\u00a1\u009c\3\2\2\2\u00a2$\3\2\2\2"+
		"\u00a3\u00a5\7\60\2\2\u00a4\u00a6\5-\27\2\u00a5\u00a4\3\2\2\2\u00a6\u00a7"+
		"\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8&\3\2\2\2\u00a9"+
		"\u00ab\5)\25\2\u00aa\u00ac\5+\26\2\u00ab\u00aa\3\2\2\2\u00ab\u00ac\3\2"+
		"\2\2\u00ac\u00ae\3\2\2\2\u00ad\u00af\5-\27\2\u00ae\u00ad\3\2\2\2\u00af"+
		"\u00b0\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1(\3\2\2\2"+
		"\u00b2\u00b3\t\4\2\2\u00b3*\3\2\2\2\u00b4\u00b5\t\5\2\2\u00b5,\3\2\2\2"+
		"\u00b6\u00b7\4\62;\2\u00b7.\3\2\2\2\u00b8\u00b9\7$\2\2\u00b9\u00ba\7$"+
		"\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00d2\6\30\6\2\u00bc\u00be\7$\2\2\u00bd"+
		"\u00bf\5\63\32\2\u00be\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00be\3"+
		"\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c3\7$\2\2\u00c3"+
		"\u00d2\3\2\2\2\u00c4\u00c5\7$\2\2\u00c5\u00c6\7$\2\2\u00c6\u00c7\7$\2"+
		"\2\u00c7\u00cb\3\2\2\2\u00c8\u00ca\5\61\31\2\u00c9\u00c8\3\2\2\2\u00ca"+
		"\u00cd\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cc\u00ce\3\2"+
		"\2\2\u00cd\u00cb\3\2\2\2\u00ce\u00cf\7$\2\2\u00cf\u00d0\7$\2\2\u00d0\u00d2"+
		"\7$\2\2\u00d1\u00b8\3\2\2\2\u00d1\u00bc\3\2\2\2\u00d1\u00c4\3\2\2\2\u00d2"+
		"\60\3\2\2\2\u00d3\u00d4\7^\2\2\u00d4\u00d5\7$\2\2\u00d5\u00d6\7$\2\2\u00d6"+
		"\u00d9\7$\2\2\u00d7\u00d9\5;\36\2\u00d8\u00d3\3\2\2\2\u00d8\u00d7\3\2"+
		"\2\2\u00d9\62\3\2\2\2\u00da\u00dc\t\16\2\2\u00db\u00da\3\2\2\2\u00dc\u00e4"+
		"\3\2\2\2\u00dd\u00de\7^\2\2\u00de\u00df\7w\2\2\u00df\u00e0\3\2\2\2\u00e0"+
		"\u00e4\5\67\34\2\u00e1\u00e2\7^\2\2\u00e2\u00e4\5\65\33\2\u00e3\u00db"+
		"\3\2\2\2\u00e3\u00dd\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e4\64\3\2\2\2\u00e5"+
		"\u00e6\t\6\2\2\u00e6\66\3\2\2\2\u00e7\u00e8\59\35\2\u00e8\u00e9\59\35"+
		"\2\u00e9\u00ea\59\35\2\u00ea\u00eb\59\35\2\u00eb\u00f5\3\2\2\2\u00ec\u00ee"+
		"\7}\2\2\u00ed\u00ef\59\35\2\u00ee\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0"+
		"\u00ee\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\7\177"+
		"\2\2\u00f3\u00f5\3\2\2\2\u00f4\u00e7\3\2\2\2\u00f4\u00ec\3\2\2\2\u00f5"+
		"8\3\2\2\2\u00f6\u00f7\t\7\2\2\u00f7:\3\2\2\2\u00f8\u00fa\t\17\2\2\u00f9"+
		"\u00f8\3\2\2\2\u00fa<\3\2\2\2\u00fb\u00fd\t\20\2\2\u00fc\u00fb\3\2\2\2"+
		"\u00fd>\3\2\2\2\u00fe\u0102\7%\2\2\u00ff\u0101\5=\37\2\u0100\u00ff\3\2"+
		"\2\2\u0101\u0104\3\2\2\2\u0102\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103"+
		"\u0105\3\2\2\2\u0104\u0102\3\2\2\2\u0105\u0106\b \2\2\u0106@\3\2\2\2\u0107"+
		"\u0108\t\b\2\2\u0108\u0109\3\2\2\2\u0109\u010a\b!\3\2\u010aB\3\2\2\2\u010b"+
		"\u010c\t\t\2\2\u010c\u010d\3\2\2\2\u010d\u010e\b\"\3\2\u010eD\3\2\2\2"+
		"\u010f\u0110\t\n\2\2\u0110\u0111\3\2\2\2\u0111\u0112\b#\3\2\u0112F\3\2"+
		"\2\2\u0113\u0114\t\13\2\2\u0114\u0115\3\2\2\2\u0115\u0116\b$\3\2\u0116"+
		"H\3\2\2\2\u0117\u0118\t\f\2\2\u0118\u0119\3\2\2\2\u0119\u011a\b%\3\2\u011a"+
		"J\3\2\2\2\u011b\u011c\7.\2\2\u011c\u011d\3\2\2\2\u011d\u011e\b&\3\2\u011e"+
		"L\3\2\2\2\u011f\u0120\t\r\2\2\u0120\u0121\3\2\2\2\u0121\u0122\b\'\3\2"+
		"\u0122N\3\2\2\2\30\2hw~\u0082\u0088\u008b\u00a1\u00a7\u00ab\u00b0\u00c0"+
		"\u00cb\u00d1\u00d8\u00db\u00e3\u00f0\u00f4\u00f9\u00fc\u0102\4\2\4\2\2"+
		"\5\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}