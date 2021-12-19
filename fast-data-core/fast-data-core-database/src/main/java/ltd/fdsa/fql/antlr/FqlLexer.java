// Generated from Fql.g4 by ANTLR 4.9.3
package ltd.fdsa.fql.antlr;
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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		BooleanValue=10, NullValue=11, FRAGMENT=12, QUERY=13, MUTATION=14, SUBSCRIPTION=15, 
		SCHEMA=16, SCALAR=17, TYPE=18, INTERFACE=19, IMPLEMENTS=20, ENUM=21, UNION=22, 
		INPUT=23, EXTEND=24, DIRECTIVE=25, ON_KEYWORD=26, REPEATABLE=27, NAME=28, 
		IntValue=29, FloatValue=30, StringValue=31, Comment=32, LF=33, CR=34, 
		LineTerminator=35, Space=36, Tab=37, Comma=38, UnicodeBOM=39;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"BooleanValue", "NullValue", "FRAGMENT", "QUERY", "MUTATION", "SUBSCRIPTION", 
			"SCHEMA", "SCALAR", "TYPE", "INTERFACE", "IMPLEMENTS", "ENUM", "UNION", 
			"INPUT", "EXTEND", "DIRECTIVE", "ON_KEYWORD", "REPEATABLE", "NAME", "IntValue", 
			"IntegerPart", "NegativeSign", "NonZeroDigit", "FloatValue", "FractionalPart", 
			"ExponentPart", "ExponentIndicator", "Sign", "Digit", "StringValue", 
			"BlockStringCharacter", "StringCharacter", "EscapedCharacter", "EscapedUnicode", 
			"Hex", "SourceCharacter", "SourceCharacterWithoutLineFeed", "Comment", 
			"LF", "CR", "LineTerminator", "Space", "Tab", "Comma", "UnicodeBOM"
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
		case 28:
			return IntValue_sempred((RuleContext)_localctx, predIndex);
		case 32:
			return FloatValue_sempred((RuleContext)_localctx, predIndex);
		case 38:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2)\u01be\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6"+
		"\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\5\13\u008b\n\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3"+
		"\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3"+
		"\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3"+
		"\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3"+
		"\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3"+
		"\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\7\35\u0111\n\35\f\35\16\35"+
		"\u0114\13\35\3\36\3\36\3\36\3\37\5\37\u011a\n\37\3\37\3\37\5\37\u011e"+
		"\n\37\3\37\3\37\7\37\u0122\n\37\f\37\16\37\u0125\13\37\5\37\u0127\n\37"+
		"\3 \3 \3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3"+
		"\"\3\"\5\"\u013d\n\"\3#\3#\6#\u0141\n#\r#\16#\u0142\3$\3$\5$\u0147\n$"+
		"\3$\6$\u014a\n$\r$\16$\u014b\3%\3%\3&\3&\3\'\3\'\3(\3(\3(\3(\3(\3(\6("+
		"\u015a\n(\r(\16(\u015b\3(\3(\3(\3(\3(\3(\3(\7(\u0165\n(\f(\16(\u0168\13"+
		"(\3(\3(\3(\5(\u016d\n(\3)\3)\3)\3)\3)\5)\u0174\n)\3*\5*\u0177\n*\3*\3"+
		"*\3*\3*\3*\3*\5*\u017f\n*\3+\3+\3,\3,\3,\3,\3,\3,\3,\6,\u018a\n,\r,\16"+
		",\u018b\3,\3,\5,\u0190\n,\3-\3-\3.\5.\u0195\n.\3/\5/\u0198\n/\3\60\3\60"+
		"\7\60\u019c\n\60\f\60\16\60\u019f\13\60\3\60\3\60\3\61\3\61\3\61\3\61"+
		"\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\65\3\65"+
		"\3\65\3\65\3\66\3\66\3\66\3\66\3\67\3\67\3\67\3\67\3\u0166\28\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37=\2?\2A\2"+
		"C E\2G\2I\2K\2M\2O!Q\2S\2U\2W\2Y\2[\2]\2_\"a#c$e%g&i\'k(m)\3\2\16\5\2"+
		"C\\aac|\6\2\62;C\\aac|\4\2GGgg\4\2--//\n\2$$\61\61^^ddhhppttvv\5\2\62"+
		";CHch\3\2\f\f\3\2\17\17\3\2\u202a\u202b\3\2\"\"\3\2\13\13\3\2\uff01\uff01"+
		"\5\b\2\2\2\13\2\r\2\16\2\20\2#\2%\2]\2_\2\ud801\2\ue002\2\1\22\4\2\2\2"+
		"\ud801\2\ue002\2\1\22\6\2\2\2\13\2\r\2\16\2\20\2\ud801\2\ue002\2\1\22"+
		"\u01c3\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2"+
		"\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2"+
		"/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2"+
		"\2\2;\3\2\2\2\2C\3\2\2\2\2O\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2"+
		"e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\3o\3\2\2\2\5q\3"+
		"\2\2\2\7s\3\2\2\2\tu\3\2\2\2\13w\3\2\2\2\ry\3\2\2\2\17{\3\2\2\2\21}\3"+
		"\2\2\2\23\177\3\2\2\2\25\u008a\3\2\2\2\27\u008c\3\2\2\2\31\u0091\3\2\2"+
		"\2\33\u009a\3\2\2\2\35\u00a0\3\2\2\2\37\u00a9\3\2\2\2!\u00b6\3\2\2\2#"+
		"\u00bd\3\2\2\2%\u00c4\3\2\2\2\'\u00c9\3\2\2\2)\u00d3\3\2\2\2+\u00de\3"+
		"\2\2\2-\u00e3\3\2\2\2/\u00e9\3\2\2\2\61\u00ef\3\2\2\2\63\u00f6\3\2\2\2"+
		"\65\u0100\3\2\2\2\67\u0103\3\2\2\29\u010e\3\2\2\2;\u0115\3\2\2\2=\u0126"+
		"\3\2\2\2?\u0128\3\2\2\2A\u012a\3\2\2\2C\u013c\3\2\2\2E\u013e\3\2\2\2G"+
		"\u0144\3\2\2\2I\u014d\3\2\2\2K\u014f\3\2\2\2M\u0151\3\2\2\2O\u016c\3\2"+
		"\2\2Q\u0173\3\2\2\2S\u017e\3\2\2\2U\u0180\3\2\2\2W\u018f\3\2\2\2Y\u0191"+
		"\3\2\2\2[\u0194\3\2\2\2]\u0197\3\2\2\2_\u0199\3\2\2\2a\u01a2\3\2\2\2c"+
		"\u01a6\3\2\2\2e\u01aa\3\2\2\2g\u01ae\3\2\2\2i\u01b2\3\2\2\2k\u01b6\3\2"+
		"\2\2m\u01ba\3\2\2\2op\7}\2\2p\4\3\2\2\2qr\7\177\2\2r\6\3\2\2\2st\7<\2"+
		"\2t\b\3\2\2\2uv\7]\2\2v\n\3\2\2\2wx\7_\2\2x\f\3\2\2\2yz\7*\2\2z\16\3\2"+
		"\2\2{|\7+\2\2|\20\3\2\2\2}~\7&\2\2~\22\3\2\2\2\177\u0080\7#\2\2\u0080"+
		"\24\3\2\2\2\u0081\u0082\7v\2\2\u0082\u0083\7t\2\2\u0083\u0084\7w\2\2\u0084"+
		"\u008b\7g\2\2\u0085\u0086\7h\2\2\u0086\u0087\7c\2\2\u0087\u0088\7n\2\2"+
		"\u0088\u0089\7u\2\2\u0089\u008b\7g\2\2\u008a\u0081\3\2\2\2\u008a\u0085"+
		"\3\2\2\2\u008b\26\3\2\2\2\u008c\u008d\7p\2\2\u008d\u008e\7w\2\2\u008e"+
		"\u008f\7n\2\2\u008f\u0090\7n\2\2\u0090\30\3\2\2\2\u0091\u0092\7h\2\2\u0092"+
		"\u0093\7t\2\2\u0093\u0094\7c\2\2\u0094\u0095\7i\2\2\u0095\u0096\7o\2\2"+
		"\u0096\u0097\7g\2\2\u0097\u0098\7p\2\2\u0098\u0099\7v\2\2\u0099\32\3\2"+
		"\2\2\u009a\u009b\7s\2\2\u009b\u009c\7w\2\2\u009c\u009d\7g\2\2\u009d\u009e"+
		"\7t\2\2\u009e\u009f\7{\2\2\u009f\34\3\2\2\2\u00a0\u00a1\7o\2\2\u00a1\u00a2"+
		"\7w\2\2\u00a2\u00a3\7v\2\2\u00a3\u00a4\7c\2\2\u00a4\u00a5\7v\2\2\u00a5"+
		"\u00a6\7k\2\2\u00a6\u00a7\7q\2\2\u00a7\u00a8\7p\2\2\u00a8\36\3\2\2\2\u00a9"+
		"\u00aa\7u\2\2\u00aa\u00ab\7w\2\2\u00ab\u00ac\7d\2\2\u00ac\u00ad\7u\2\2"+
		"\u00ad\u00ae\7e\2\2\u00ae\u00af\7t\2\2\u00af\u00b0\7k\2\2\u00b0\u00b1"+
		"\7r\2\2\u00b1\u00b2\7v\2\2\u00b2\u00b3\7k\2\2\u00b3\u00b4\7q\2\2\u00b4"+
		"\u00b5\7p\2\2\u00b5 \3\2\2\2\u00b6\u00b7\7u\2\2\u00b7\u00b8\7e\2\2\u00b8"+
		"\u00b9\7j\2\2\u00b9\u00ba\7g\2\2\u00ba\u00bb\7o\2\2\u00bb\u00bc\7c\2\2"+
		"\u00bc\"\3\2\2\2\u00bd\u00be\7u\2\2\u00be\u00bf\7e\2\2\u00bf\u00c0\7c"+
		"\2\2\u00c0\u00c1\7n\2\2\u00c1\u00c2\7c\2\2\u00c2\u00c3\7t\2\2\u00c3$\3"+
		"\2\2\2\u00c4\u00c5\7v\2\2\u00c5\u00c6\7{\2\2\u00c6\u00c7\7r\2\2\u00c7"+
		"\u00c8\7g\2\2\u00c8&\3\2\2\2\u00c9\u00ca\7k\2\2\u00ca\u00cb\7p\2\2\u00cb"+
		"\u00cc\7v\2\2\u00cc\u00cd\7g\2\2\u00cd\u00ce\7t\2\2\u00ce\u00cf\7h\2\2"+
		"\u00cf\u00d0\7c\2\2\u00d0\u00d1\7e\2\2\u00d1\u00d2\7g\2\2\u00d2(\3\2\2"+
		"\2\u00d3\u00d4\7k\2\2\u00d4\u00d5\7o\2\2\u00d5\u00d6\7r\2\2\u00d6\u00d7"+
		"\7n\2\2\u00d7\u00d8\7g\2\2\u00d8\u00d9\7o\2\2\u00d9\u00da\7g\2\2\u00da"+
		"\u00db\7p\2\2\u00db\u00dc\7v\2\2\u00dc\u00dd\7u\2\2\u00dd*\3\2\2\2\u00de"+
		"\u00df\7g\2\2\u00df\u00e0\7p\2\2\u00e0\u00e1\7w\2\2\u00e1\u00e2\7o\2\2"+
		"\u00e2,\3\2\2\2\u00e3\u00e4\7w\2\2\u00e4\u00e5\7p\2\2\u00e5\u00e6\7k\2"+
		"\2\u00e6\u00e7\7q\2\2\u00e7\u00e8\7p\2\2\u00e8.\3\2\2\2\u00e9\u00ea\7"+
		"k\2\2\u00ea\u00eb\7p\2\2\u00eb\u00ec\7r\2\2\u00ec\u00ed\7w\2\2\u00ed\u00ee"+
		"\7v\2\2\u00ee\60\3\2\2\2\u00ef\u00f0\7g\2\2\u00f0\u00f1\7z\2\2\u00f1\u00f2"+
		"\7v\2\2\u00f2\u00f3\7g\2\2\u00f3\u00f4\7p\2\2\u00f4\u00f5\7f\2\2\u00f5"+
		"\62\3\2\2\2\u00f6\u00f7\7f\2\2\u00f7\u00f8\7k\2\2\u00f8\u00f9\7t\2\2\u00f9"+
		"\u00fa\7g\2\2\u00fa\u00fb\7e\2\2\u00fb\u00fc\7v\2\2\u00fc\u00fd\7k\2\2"+
		"\u00fd\u00fe\7x\2\2\u00fe\u00ff\7g\2\2\u00ff\64\3\2\2\2\u0100\u0101\7"+
		"q\2\2\u0101\u0102\7p\2\2\u0102\66\3\2\2\2\u0103\u0104\7t\2\2\u0104\u0105"+
		"\7g\2\2\u0105\u0106\7r\2\2\u0106\u0107\7g\2\2\u0107\u0108\7c\2\2\u0108"+
		"\u0109\7v\2\2\u0109\u010a\7c\2\2\u010a\u010b\7d\2\2\u010b\u010c\7n\2\2"+
		"\u010c\u010d\7g\2\2\u010d8\3\2\2\2\u010e\u0112\t\2\2\2\u010f\u0111\t\3"+
		"\2\2\u0110\u010f\3\2\2\2\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112"+
		"\u0113\3\2\2\2\u0113:\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0116\5=\37\2"+
		"\u0116\u0117\6\36\2\2\u0117<\3\2\2\2\u0118\u011a\5? \2\u0119\u0118\3\2"+
		"\2\2\u0119\u011a\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u0127\7\62\2\2\u011c"+
		"\u011e\5? \2\u011d\u011c\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u011f\3\2\2"+
		"\2\u011f\u0123\5A!\2\u0120\u0122\5M\'\2\u0121\u0120\3\2\2\2\u0122\u0125"+
		"\3\2\2\2\u0123\u0121\3\2\2\2\u0123\u0124\3\2\2\2\u0124\u0127\3\2\2\2\u0125"+
		"\u0123\3\2\2\2\u0126\u0119\3\2\2\2\u0126\u011d\3\2\2\2\u0127>\3\2\2\2"+
		"\u0128\u0129\7/\2\2\u0129@\3\2\2\2\u012a\u012b\4\63;\2\u012bB\3\2\2\2"+
		"\u012c\u012d\5=\37\2\u012d\u012e\5E#\2\u012e\u012f\5G$\2\u012f\u0130\3"+
		"\2\2\2\u0130\u0131\6\"\3\2\u0131\u013d\3\2\2\2\u0132\u0133\5=\37\2\u0133"+
		"\u0134\5E#\2\u0134\u0135\3\2\2\2\u0135\u0136\6\"\4\2\u0136\u013d\3\2\2"+
		"\2\u0137\u0138\5=\37\2\u0138\u0139\5G$\2\u0139\u013a\3\2\2\2\u013a\u013b"+
		"\6\"\5\2\u013b\u013d\3\2\2\2\u013c\u012c\3\2\2\2\u013c\u0132\3\2\2\2\u013c"+
		"\u0137\3\2\2\2\u013dD\3\2\2\2\u013e\u0140\7\60\2\2\u013f\u0141\5M\'\2"+
		"\u0140\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\u0140\3\2\2\2\u0142\u0143"+
		"\3\2\2\2\u0143F\3\2\2\2\u0144\u0146\5I%\2\u0145\u0147\5K&\2\u0146\u0145"+
		"\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0149\3\2\2\2\u0148\u014a\5M\'\2\u0149"+
		"\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u0149\3\2\2\2\u014b\u014c\3\2"+
		"\2\2\u014cH\3\2\2\2\u014d\u014e\t\4\2\2\u014eJ\3\2\2\2\u014f\u0150\t\5"+
		"\2\2\u0150L\3\2\2\2\u0151\u0152\4\62;\2\u0152N\3\2\2\2\u0153\u0154\7$"+
		"\2\2\u0154\u0155\7$\2\2\u0155\u0156\3\2\2\2\u0156\u016d\6(\6\2\u0157\u0159"+
		"\7$\2\2\u0158\u015a\5S*\2\u0159\u0158\3\2\2\2\u015a\u015b\3\2\2\2\u015b"+
		"\u0159\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u015e\7$"+
		"\2\2\u015e\u016d\3\2\2\2\u015f\u0160\7$\2\2\u0160\u0161\7$\2\2\u0161\u0162"+
		"\7$\2\2\u0162\u0166\3\2\2\2\u0163\u0165\5Q)\2\u0164\u0163\3\2\2\2\u0165"+
		"\u0168\3\2\2\2\u0166\u0167\3\2\2\2\u0166\u0164\3\2\2\2\u0167\u0169\3\2"+
		"\2\2\u0168\u0166\3\2\2\2\u0169\u016a\7$\2\2\u016a\u016b\7$\2\2\u016b\u016d"+
		"\7$\2\2\u016c\u0153\3\2\2\2\u016c\u0157\3\2\2\2\u016c\u015f\3\2\2\2\u016d"+
		"P\3\2\2\2\u016e\u016f\7^\2\2\u016f\u0170\7$\2\2\u0170\u0171\7$\2\2\u0171"+
		"\u0174\7$\2\2\u0172\u0174\5[.\2\u0173\u016e\3\2\2\2\u0173\u0172\3\2\2"+
		"\2\u0174R\3\2\2\2\u0175\u0177\t\16\2\2\u0176\u0175\3\2\2\2\u0177\u017f"+
		"\3\2\2\2\u0178\u0179\7^\2\2\u0179\u017a\7w\2\2\u017a\u017b\3\2\2\2\u017b"+
		"\u017f\5W,\2\u017c\u017d\7^\2\2\u017d\u017f\5U+\2\u017e\u0176\3\2\2\2"+
		"\u017e\u0178\3\2\2\2\u017e\u017c\3\2\2\2\u017fT\3\2\2\2\u0180\u0181\t"+
		"\6\2\2\u0181V\3\2\2\2\u0182\u0183\5Y-\2\u0183\u0184\5Y-\2\u0184\u0185"+
		"\5Y-\2\u0185\u0186\5Y-\2\u0186\u0190\3\2\2\2\u0187\u0189\7}\2\2\u0188"+
		"\u018a\5Y-\2\u0189\u0188\3\2\2\2\u018a\u018b\3\2\2\2\u018b\u0189\3\2\2"+
		"\2\u018b\u018c\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e\7\177\2\2\u018e"+
		"\u0190\3\2\2\2\u018f\u0182\3\2\2\2\u018f\u0187\3\2\2\2\u0190X\3\2\2\2"+
		"\u0191\u0192\t\7\2\2\u0192Z\3\2\2\2\u0193\u0195\t\17\2\2\u0194\u0193\3"+
		"\2\2\2\u0195\\\3\2\2\2\u0196\u0198\t\20\2\2\u0197\u0196\3\2\2\2\u0198"+
		"^\3\2\2\2\u0199\u019d\7%\2\2\u019a\u019c\5]/\2\u019b\u019a\3\2\2\2\u019c"+
		"\u019f\3\2\2\2\u019d\u019b\3\2\2\2\u019d\u019e\3\2\2\2\u019e\u01a0\3\2"+
		"\2\2\u019f\u019d\3\2\2\2\u01a0\u01a1\b\60\2\2\u01a1`\3\2\2\2\u01a2\u01a3"+
		"\t\b\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01a5\b\61\3\2\u01a5b\3\2\2\2\u01a6"+
		"\u01a7\t\t\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a9\b\62\3\2\u01a9d\3\2\2\2"+
		"\u01aa\u01ab\t\n\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ad\b\63\3\2\u01adf\3"+
		"\2\2\2\u01ae\u01af\t\13\2\2\u01af\u01b0\3\2\2\2\u01b0\u01b1\b\64\3\2\u01b1"+
		"h\3\2\2\2\u01b2\u01b3\t\f\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b5\b\65\3\2"+
		"\u01b5j\3\2\2\2\u01b6\u01b7\7.\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01b9\b\66"+
		"\3\2\u01b9l\3\2\2\2\u01ba\u01bb\t\r\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01bd"+
		"\b\67\3\2\u01bdn\3\2\2\2\30\2\u008a\u0112\u0119\u011d\u0123\u0126\u013c"+
		"\u0142\u0146\u014b\u015b\u0166\u016c\u0173\u0176\u017e\u018b\u018f\u0194"+
		"\u0197\u019d\4\2\4\2\2\5\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}