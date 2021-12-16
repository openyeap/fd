// Generated from Graphql.g4 by ANTLR 4.9.3
package ltd.fdsa.antlr.fql;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GraphqlLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, BooleanValue=11, NullValue=12, FRAGMENT=13, QUERY=14, MUTATION=15, 
		SUBSCRIPTION=16, SCHEMA=17, SCALAR=18, TYPE=19, INTERFACE=20, IMPLEMENTS=21, 
		ENUM=22, UNION=23, INPUT=24, EXTEND=25, DIRECTIVE=26, ON_KEYWORD=27, REPEATABLE=28, 
		NAME=29, IntValue=30, FloatValue=31, StringValue=32, Comment=33, LF=34, 
		CR=35, LineTerminator=36, Space=37, Tab=38, Comma=39, UnicodeBOM=40;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "BooleanValue", "NullValue", "FRAGMENT", "QUERY", "MUTATION", 
			"SUBSCRIPTION", "SCHEMA", "SCALAR", "TYPE", "INTERFACE", "IMPLEMENTS", 
			"ENUM", "UNION", "INPUT", "EXTEND", "DIRECTIVE", "ON_KEYWORD", "REPEATABLE", 
			"NAME", "IntValue", "IntegerPart", "NegativeSign", "NonZeroDigit", "FloatValue", 
			"FractionalPart", "ExponentPart", "ExponentIndicator", "Sign", "Digit", 
			"StringValue", "BlockStringCharacter", "StringCharacter", "EscapedCharacter", 
			"EscapedUnicode", "Hex", "SourceCharacter", "SourceCharacterWithoutLineFeed", 
			"Comment", "LF", "CR", "LineTerminator", "Space", "Tab", "Comma", "UnicodeBOM"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "':'", "'['", "']'", "'@'", "'('", "')'", "'$'", 
			"'!'", null, "'null'", "'fragment'", "'query'", "'mutation'", "'subscription'", 
			"'schema'", "'scalar'", "'type'", "'interface'", "'implements'", "'enum'", 
			"'union'", "'input'", "'extend'", "'directive'", "'on'", "'repeatable'", 
			null, null, null, null, null, null, null, null, null, null, "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "BooleanValue", 
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

	    public boolean isDigit(int c) {        return c >= '0' && c <= '9';    }    public boolean isNameStart(int c) {        return '_' == c ||          (c >= 'A' && c <= 'Z') ||          (c >= 'a' && c <= 'z');    }    public boolean isDot(int c) {        return '.' == c;    }

	public GraphqlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Graphql.g4"; }

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
		case 29:
			return IntValue_sempred((RuleContext)_localctx, predIndex);
		case 33:
			return FloatValue_sempred((RuleContext)_localctx, predIndex);
		case 39:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2*\u01c2\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\5\f\u008f\n\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3"+
		"\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3"+
		"\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3"+
		"\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3"+
		"\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3"+
		"\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\7\36\u0115"+
		"\n\36\f\36\16\36\u0118\13\36\3\37\3\37\3\37\3 \5 \u011e\n \3 \3 \5 \u0122"+
		"\n \3 \3 \7 \u0126\n \f \16 \u0129\13 \5 \u012b\n \3!\3!\3\"\3\"\3#\3"+
		"#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\5#\u0141\n#\3$\3$\6$\u0145"+
		"\n$\r$\16$\u0146\3%\3%\5%\u014b\n%\3%\6%\u014e\n%\r%\16%\u014f\3&\3&\3"+
		"\'\3\'\3(\3(\3)\3)\3)\3)\3)\3)\6)\u015e\n)\r)\16)\u015f\3)\3)\3)\3)\3"+
		")\3)\3)\7)\u0169\n)\f)\16)\u016c\13)\3)\3)\3)\5)\u0171\n)\3*\3*\3*\3*"+
		"\3*\5*\u0178\n*\3+\5+\u017b\n+\3+\3+\3+\3+\3+\3+\5+\u0183\n+\3,\3,\3-"+
		"\3-\3-\3-\3-\3-\3-\6-\u018e\n-\r-\16-\u018f\3-\3-\5-\u0194\n-\3.\3.\3"+
		"/\5/\u0199\n/\3\60\5\60\u019c\n\60\3\61\3\61\7\61\u01a0\n\61\f\61\16\61"+
		"\u01a3\13\61\3\61\3\61\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\64\3"+
		"\64\3\64\3\64\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\67\3\67\3\67\3"+
		"\67\38\38\38\38\3\u016a\29\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32"+
		"\63\33\65\34\67\359\36;\37= ?\2A\2C\2E!G\2I\2K\2M\2O\2Q\"S\2U\2W\2Y\2"+
		"[\2]\2_\2a#c$e%g&i\'k(m)o*\3\2\16\5\2C\\aac|\6\2\62;C\\aac|\4\2GGgg\4"+
		"\2--//\n\2$$\61\61^^ddhhppttvv\5\2\62;CHch\3\2\f\f\3\2\17\17\3\2\u202a"+
		"\u202b\3\2\"\"\3\2\13\13\3\2\uff01\uff01\5\b\2\2\2\13\2\r\2\16\2\20\2"+
		"#\2%\2]\2_\2\ud801\2\ue002\2\1\22\4\2\2\2\ud801\2\ue002\2\1\22\6\2\2\2"+
		"\13\2\r\2\16\2\20\2\ud801\2\ue002\2\1\22\u01c7\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2"+
		"\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2"+
		"\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2E\3\2"+
		"\2\2\2Q\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2"+
		"\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\3q\3\2\2\2\5s\3\2\2\2\7u\3\2\2\2\tw"+
		"\3\2\2\2\13y\3\2\2\2\r{\3\2\2\2\17}\3\2\2\2\21\177\3\2\2\2\23\u0081\3"+
		"\2\2\2\25\u0083\3\2\2\2\27\u008e\3\2\2\2\31\u0090\3\2\2\2\33\u0095\3\2"+
		"\2\2\35\u009e\3\2\2\2\37\u00a4\3\2\2\2!\u00ad\3\2\2\2#\u00ba\3\2\2\2%"+
		"\u00c1\3\2\2\2\'\u00c8\3\2\2\2)\u00cd\3\2\2\2+\u00d7\3\2\2\2-\u00e2\3"+
		"\2\2\2/\u00e7\3\2\2\2\61\u00ed\3\2\2\2\63\u00f3\3\2\2\2\65\u00fa\3\2\2"+
		"\2\67\u0104\3\2\2\29\u0107\3\2\2\2;\u0112\3\2\2\2=\u0119\3\2\2\2?\u012a"+
		"\3\2\2\2A\u012c\3\2\2\2C\u012e\3\2\2\2E\u0140\3\2\2\2G\u0142\3\2\2\2I"+
		"\u0148\3\2\2\2K\u0151\3\2\2\2M\u0153\3\2\2\2O\u0155\3\2\2\2Q\u0170\3\2"+
		"\2\2S\u0177\3\2\2\2U\u0182\3\2\2\2W\u0184\3\2\2\2Y\u0193\3\2\2\2[\u0195"+
		"\3\2\2\2]\u0198\3\2\2\2_\u019b\3\2\2\2a\u019d\3\2\2\2c\u01a6\3\2\2\2e"+
		"\u01aa\3\2\2\2g\u01ae\3\2\2\2i\u01b2\3\2\2\2k\u01b6\3\2\2\2m\u01ba\3\2"+
		"\2\2o\u01be\3\2\2\2qr\7}\2\2r\4\3\2\2\2st\7\177\2\2t\6\3\2\2\2uv\7<\2"+
		"\2v\b\3\2\2\2wx\7]\2\2x\n\3\2\2\2yz\7_\2\2z\f\3\2\2\2{|\7B\2\2|\16\3\2"+
		"\2\2}~\7*\2\2~\20\3\2\2\2\177\u0080\7+\2\2\u0080\22\3\2\2\2\u0081\u0082"+
		"\7&\2\2\u0082\24\3\2\2\2\u0083\u0084\7#\2\2\u0084\26\3\2\2\2\u0085\u0086"+
		"\7v\2\2\u0086\u0087\7t\2\2\u0087\u0088\7w\2\2\u0088\u008f\7g\2\2\u0089"+
		"\u008a\7h\2\2\u008a\u008b\7c\2\2\u008b\u008c\7n\2\2\u008c\u008d\7u\2\2"+
		"\u008d\u008f\7g\2\2\u008e\u0085\3\2\2\2\u008e\u0089\3\2\2\2\u008f\30\3"+
		"\2\2\2\u0090\u0091\7p\2\2\u0091\u0092\7w\2\2\u0092\u0093\7n\2\2\u0093"+
		"\u0094\7n\2\2\u0094\32\3\2\2\2\u0095\u0096\7h\2\2\u0096\u0097\7t\2\2\u0097"+
		"\u0098\7c\2\2\u0098\u0099\7i\2\2\u0099\u009a\7o\2\2\u009a\u009b\7g\2\2"+
		"\u009b\u009c\7p\2\2\u009c\u009d\7v\2\2\u009d\34\3\2\2\2\u009e\u009f\7"+
		"s\2\2\u009f\u00a0\7w\2\2\u00a0\u00a1\7g\2\2\u00a1\u00a2\7t\2\2\u00a2\u00a3"+
		"\7{\2\2\u00a3\36\3\2\2\2\u00a4\u00a5\7o\2\2\u00a5\u00a6\7w\2\2\u00a6\u00a7"+
		"\7v\2\2\u00a7\u00a8\7c\2\2\u00a8\u00a9\7v\2\2\u00a9\u00aa\7k\2\2\u00aa"+
		"\u00ab\7q\2\2\u00ab\u00ac\7p\2\2\u00ac \3\2\2\2\u00ad\u00ae\7u\2\2\u00ae"+
		"\u00af\7w\2\2\u00af\u00b0\7d\2\2\u00b0\u00b1\7u\2\2\u00b1\u00b2\7e\2\2"+
		"\u00b2\u00b3\7t\2\2\u00b3\u00b4\7k\2\2\u00b4\u00b5\7r\2\2\u00b5\u00b6"+
		"\7v\2\2\u00b6\u00b7\7k\2\2\u00b7\u00b8\7q\2\2\u00b8\u00b9\7p\2\2\u00b9"+
		"\"\3\2\2\2\u00ba\u00bb\7u\2\2\u00bb\u00bc\7e\2\2\u00bc\u00bd\7j\2\2\u00bd"+
		"\u00be\7g\2\2\u00be\u00bf\7o\2\2\u00bf\u00c0\7c\2\2\u00c0$\3\2\2\2\u00c1"+
		"\u00c2\7u\2\2\u00c2\u00c3\7e\2\2\u00c3\u00c4\7c\2\2\u00c4\u00c5\7n\2\2"+
		"\u00c5\u00c6\7c\2\2\u00c6\u00c7\7t\2\2\u00c7&\3\2\2\2\u00c8\u00c9\7v\2"+
		"\2\u00c9\u00ca\7{\2\2\u00ca\u00cb\7r\2\2\u00cb\u00cc\7g\2\2\u00cc(\3\2"+
		"\2\2\u00cd\u00ce\7k\2\2\u00ce\u00cf\7p\2\2\u00cf\u00d0\7v\2\2\u00d0\u00d1"+
		"\7g\2\2\u00d1\u00d2\7t\2\2\u00d2\u00d3\7h\2\2\u00d3\u00d4\7c\2\2\u00d4"+
		"\u00d5\7e\2\2\u00d5\u00d6\7g\2\2\u00d6*\3\2\2\2\u00d7\u00d8\7k\2\2\u00d8"+
		"\u00d9\7o\2\2\u00d9\u00da\7r\2\2\u00da\u00db\7n\2\2\u00db\u00dc\7g\2\2"+
		"\u00dc\u00dd\7o\2\2\u00dd\u00de\7g\2\2\u00de\u00df\7p\2\2\u00df\u00e0"+
		"\7v\2\2\u00e0\u00e1\7u\2\2\u00e1,\3\2\2\2\u00e2\u00e3\7g\2\2\u00e3\u00e4"+
		"\7p\2\2\u00e4\u00e5\7w\2\2\u00e5\u00e6\7o\2\2\u00e6.\3\2\2\2\u00e7\u00e8"+
		"\7w\2\2\u00e8\u00e9\7p\2\2\u00e9\u00ea\7k\2\2\u00ea\u00eb\7q\2\2\u00eb"+
		"\u00ec\7p\2\2\u00ec\60\3\2\2\2\u00ed\u00ee\7k\2\2\u00ee\u00ef\7p\2\2\u00ef"+
		"\u00f0\7r\2\2\u00f0\u00f1\7w\2\2\u00f1\u00f2\7v\2\2\u00f2\62\3\2\2\2\u00f3"+
		"\u00f4\7g\2\2\u00f4\u00f5\7z\2\2\u00f5\u00f6\7v\2\2\u00f6\u00f7\7g\2\2"+
		"\u00f7\u00f8\7p\2\2\u00f8\u00f9\7f\2\2\u00f9\64\3\2\2\2\u00fa\u00fb\7"+
		"f\2\2\u00fb\u00fc\7k\2\2\u00fc\u00fd\7t\2\2\u00fd\u00fe\7g\2\2\u00fe\u00ff"+
		"\7e\2\2\u00ff\u0100\7v\2\2\u0100\u0101\7k\2\2\u0101\u0102\7x\2\2\u0102"+
		"\u0103\7g\2\2\u0103\66\3\2\2\2\u0104\u0105\7q\2\2\u0105\u0106\7p\2\2\u0106"+
		"8\3\2\2\2\u0107\u0108\7t\2\2\u0108\u0109\7g\2\2\u0109\u010a\7r\2\2\u010a"+
		"\u010b\7g\2\2\u010b\u010c\7c\2\2\u010c\u010d\7v\2\2\u010d\u010e\7c\2\2"+
		"\u010e\u010f\7d\2\2\u010f\u0110\7n\2\2\u0110\u0111\7g\2\2\u0111:\3\2\2"+
		"\2\u0112\u0116\t\2\2\2\u0113\u0115\t\3\2\2\u0114\u0113\3\2\2\2\u0115\u0118"+
		"\3\2\2\2\u0116\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117<\3\2\2\2\u0118"+
		"\u0116\3\2\2\2\u0119\u011a\5? \2\u011a\u011b\6\37\2\2\u011b>\3\2\2\2\u011c"+
		"\u011e\5A!\2\u011d\u011c\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u011f\3\2\2"+
		"\2\u011f\u012b\7\62\2\2\u0120\u0122\5A!\2\u0121\u0120\3\2\2\2\u0121\u0122"+
		"\3\2\2\2\u0122\u0123\3\2\2\2\u0123\u0127\5C\"\2\u0124\u0126\5O(\2\u0125"+
		"\u0124\3\2\2\2\u0126\u0129\3\2\2\2\u0127\u0125\3\2\2\2\u0127\u0128\3\2"+
		"\2\2\u0128\u012b\3\2\2\2\u0129\u0127\3\2\2\2\u012a\u011d\3\2\2\2\u012a"+
		"\u0121\3\2\2\2\u012b@\3\2\2\2\u012c\u012d\7/\2\2\u012dB\3\2\2\2\u012e"+
		"\u012f\4\63;\2\u012fD\3\2\2\2\u0130\u0131\5? \2\u0131\u0132\5G$\2\u0132"+
		"\u0133\5I%\2\u0133\u0134\3\2\2\2\u0134\u0135\6#\3\2\u0135\u0141\3\2\2"+
		"\2\u0136\u0137\5? \2\u0137\u0138\5G$\2\u0138\u0139\3\2\2\2\u0139\u013a"+
		"\6#\4\2\u013a\u0141\3\2\2\2\u013b\u013c\5? \2\u013c\u013d\5I%\2\u013d"+
		"\u013e\3\2\2\2\u013e\u013f\6#\5\2\u013f\u0141\3\2\2\2\u0140\u0130\3\2"+
		"\2\2\u0140\u0136\3\2\2\2\u0140\u013b\3\2\2\2\u0141F\3\2\2\2\u0142\u0144"+
		"\7\60\2\2\u0143\u0145\5O(\2\u0144\u0143\3\2\2\2\u0145\u0146\3\2\2\2\u0146"+
		"\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u0147H\3\2\2\2\u0148\u014a\5K&\2\u0149"+
		"\u014b\5M\'\2\u014a\u0149\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u014d\3\2"+
		"\2\2\u014c\u014e\5O(\2\u014d\u014c\3\2\2\2\u014e\u014f\3\2\2\2\u014f\u014d"+
		"\3\2\2\2\u014f\u0150\3\2\2\2\u0150J\3\2\2\2\u0151\u0152\t\4\2\2\u0152"+
		"L\3\2\2\2\u0153\u0154\t\5\2\2\u0154N\3\2\2\2\u0155\u0156\4\62;\2\u0156"+
		"P\3\2\2\2\u0157\u0158\7$\2\2\u0158\u0159\7$\2\2\u0159\u015a\3\2\2\2\u015a"+
		"\u0171\6)\6\2\u015b\u015d\7$\2\2\u015c\u015e\5U+\2\u015d\u015c\3\2\2\2"+
		"\u015e\u015f\3\2\2\2\u015f\u015d\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u0161"+
		"\3\2\2\2\u0161\u0162\7$\2\2\u0162\u0171\3\2\2\2\u0163\u0164\7$\2\2\u0164"+
		"\u0165\7$\2\2\u0165\u0166\7$\2\2\u0166\u016a\3\2\2\2\u0167\u0169\5S*\2"+
		"\u0168\u0167\3\2\2\2\u0169\u016c\3\2\2\2\u016a\u016b\3\2\2\2\u016a\u0168"+
		"\3\2\2\2\u016b\u016d\3\2\2\2\u016c\u016a\3\2\2\2\u016d\u016e\7$\2\2\u016e"+
		"\u016f\7$\2\2\u016f\u0171\7$\2\2\u0170\u0157\3\2\2\2\u0170\u015b\3\2\2"+
		"\2\u0170\u0163\3\2\2\2\u0171R\3\2\2\2\u0172\u0173\7^\2\2\u0173\u0174\7"+
		"$\2\2\u0174\u0175\7$\2\2\u0175\u0178\7$\2\2\u0176\u0178\5]/\2\u0177\u0172"+
		"\3\2\2\2\u0177\u0176\3\2\2\2\u0178T\3\2\2\2\u0179\u017b\t\16\2\2\u017a"+
		"\u0179\3\2\2\2\u017b\u0183\3\2\2\2\u017c\u017d\7^\2\2\u017d\u017e\7w\2"+
		"\2\u017e\u017f\3\2\2\2\u017f\u0183\5Y-\2\u0180\u0181\7^\2\2\u0181\u0183"+
		"\5W,\2\u0182\u017a\3\2\2\2\u0182\u017c\3\2\2\2\u0182\u0180\3\2\2\2\u0183"+
		"V\3\2\2\2\u0184\u0185\t\6\2\2\u0185X\3\2\2\2\u0186\u0187\5[.\2\u0187\u0188"+
		"\5[.\2\u0188\u0189\5[.\2\u0189\u018a\5[.\2\u018a\u0194\3\2\2\2\u018b\u018d"+
		"\7}\2\2\u018c\u018e\5[.\2\u018d\u018c\3\2\2\2\u018e\u018f\3\2\2\2\u018f"+
		"\u018d\3\2\2\2\u018f\u0190\3\2\2\2\u0190\u0191\3\2\2\2\u0191\u0192\7\177"+
		"\2\2\u0192\u0194\3\2\2\2\u0193\u0186\3\2\2\2\u0193\u018b\3\2\2\2\u0194"+
		"Z\3\2\2\2\u0195\u0196\t\7\2\2\u0196\\\3\2\2\2\u0197\u0199\t\17\2\2\u0198"+
		"\u0197\3\2\2\2\u0199^\3\2\2\2\u019a\u019c\t\20\2\2\u019b\u019a\3\2\2\2"+
		"\u019c`\3\2\2\2\u019d\u01a1\7%\2\2\u019e\u01a0\5_\60\2\u019f\u019e\3\2"+
		"\2\2\u01a0\u01a3\3\2\2\2\u01a1\u019f\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2"+
		"\u01a4\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a4\u01a5\b\61\2\2\u01a5b\3\2\2\2"+
		"\u01a6\u01a7\t\b\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a9\b\62\3\2\u01a9d\3"+
		"\2\2\2\u01aa\u01ab\t\t\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ad\b\63\3\2\u01ad"+
		"f\3\2\2\2\u01ae\u01af\t\n\2\2\u01af\u01b0\3\2\2\2\u01b0\u01b1\b\64\3\2"+
		"\u01b1h\3\2\2\2\u01b2\u01b3\t\13\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b5\b"+
		"\65\3\2\u01b5j\3\2\2\2\u01b6\u01b7\t\f\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01b9"+
		"\b\66\3\2\u01b9l\3\2\2\2\u01ba\u01bb\7.\2\2\u01bb\u01bc\3\2\2\2\u01bc"+
		"\u01bd\b\67\3\2\u01bdn\3\2\2\2\u01be\u01bf\t\r\2\2\u01bf\u01c0\3\2\2\2"+
		"\u01c0\u01c1\b8\3\2\u01c1p\3\2\2\2\30\2\u008e\u0116\u011d\u0121\u0127"+
		"\u012a\u0140\u0146\u014a\u014f\u015f\u016a\u0170\u0177\u017a\u0182\u018f"+
		"\u0193\u0198\u019b\u01a1\4\2\4\2\2\5\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}