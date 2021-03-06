
// ---------------------------------------------------------------------------------------------------------
// --- header information
// ---------------------------------------------------------------------------------------------------------


// imports
package name.martingeisse.phunky.runtime.parser;
import java.math.BigInteger;
import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.Location;
import name.martingeisse.phunky.runtime.code.expression.LocalVariableExpression;

/**
 * The Lexer.
 */
%%
%class Lexer
%public
%unicode
%cup
%line
%column
%eofval{
	Location location = new Location(filePath, yyline, yycolumn);
	return symbolFactory.newSymbol("EOF", Tokens.EOF, location, location); 
%eofval}
%{

	private String filePath = null;
	private ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
	private StringBuilder stringBuilder = new StringBuilder();
	private String heredocNowdocMarker;
	private boolean heredocNowdocVariableInterpolation;

	/**
	 * Setter method for the filePath.
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private Symbol symbol(int type) {
		String text = yytext();
		Location left = new Location(filePath, yyline, yycolumn);
		Location right = new Location(filePath, yyline, yycolumn + text.length());
		return symbolFactory.newSymbol(text, type, left, right);
	}
  
	private Symbol symbol(int type, Object value) {
		String text = yytext();
		Location left = new Location(filePath, yyline, yycolumn);
		Location right = new Location(filePath, yyline, yycolumn + text.length());
		return symbolFactory.newSymbol(text, type, left, right, value);
	}
	
	private void beginHeredocNowdoc(String markerContainer, boolean markerQuoted, boolean interpolateVariables) {
		markerContainer = markerContainer.trim();
		this.heredocNowdocMarker = markerContainer.substring(markerQuoted ? 4 : 3, markerContainer.length() - (markerQuoted ? 1 : 0));
		this.heredocNowdocVariableInterpolation = interpolateVariables;
		stringBuilder.setLength(0);
		yybegin(HEREDOC_NOWDOC);
	}
	
	private Symbol handleHeredocNowdocStopperCandidate(String identifier) {
		if (identifier.equals(heredocNowdocMarker)) {
			return buildHeredocNowdocString();
		} else {
			handleHeredocNowdocContent(identifier);
			return null;
		}
	}
	
	private void handleHeredocNowdocContent(String content) {
		stringBuilder.append(content);
	}
	
	private Symbol buildHeredocNowdocString() {
		// the newline character that separates the content from the end marker
		// is NOT part of the content
		if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '\n') {
			stringBuilder.setLength(stringBuilder.length() - 1);
		}
	
		yybegin(CODE);
		int token = (heredocNowdocVariableInterpolation ? Tokens.DOUBLE_QUOTED_STRING_LITERAL : Tokens.SINGLE_QUOTED_STRING_LITERAL);
		return symbol(token, stringBuilder.toString());
	}
	
	// may yield a float due to overflow
	private Number parseIntegerLiteral(String text, int radix) {
		try {
			return Long.parseLong(text, radix);
		} catch (NumberFormatException e) {
			return new BigInteger(text, radix).doubleValue();
		}
	}
	
%}



// ---------------------------------------------------------------------------------------------------------
// --- helper regexes
// ---------------------------------------------------------------------------------------------------------


// white space
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

// comments
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*

// numbers
// note the braindead syntax for octals: the literals *may* contain '8' and
// '9' digits, causing that and the remaining digits to be skipped as if they
// weren't there
DecimalIntegerLiteral = "0" | ([1-9][0-9]*)
HexadecimalIntegerLiteral = "0x" [0-9a-fA-F]+
OctalIntegerLiteral = "0" [0-9]+
BinaryIntegerLiteral = ("0b" | "0B") [01]+
FloatLiteral = [\+\-]? ([0-9]+ "." [0-9]* | [0-9]* "." [0-9]+) ([eE] [\+\-]? [0-9]+)?

// identifiers and keywords
Identifier = [\p{Alpha}\_] [\p{Alnum}\_]*
LocalVariableSingleIndirection = "$" {Identifier}
LocalVariableMultiIndirection = "$"+ {Identifier}

// heredoc and nowdoc
UnquotedHeredocStarter = "<<<" {Identifier} {LineTerminator}
QuotedHeredocStarter = "<<<\"" {Identifier} "\"" {LineTerminator}
NowdocStarter = "<<<'" {Identifier} "'" {LineTerminator}
HeredocNowdocContentLine = .* {LineTerminator}

// ---------------------------------------------------------------------------------------------------------
// --- lexer rules
// ---------------------------------------------------------------------------------------------------------


%state CODE
%state SINGLE_QUOTED_STRING
%state DOUBLE_QUOTED_STRING
%state HEREDOC_NOWDOC
%%

// verbatim content outside the PHP tags
<YYINITIAL> {

	// actual verbatim content
	[^<]+ {
		return symbol(Tokens.VERBATIM_CONTENT, yytext());
	}
	"<" / [^?] {
		return symbol(Tokens.VERBATIM_CONTENT, yytext());
	}
	
	// statement opening tag
	"<?" {
		yybegin(CODE);
	}
	"<?php" {
		yybegin(CODE);
	}
	
	// echo opening tag
	"<?=" {
		yybegin(CODE);
		return symbol(Tokens.IDENTIFIER, "echo"); 
	}
	
}

// normal scanning
<CODE> {

	// PHP closing tag (back to verbatim content)
	"?>" \n? {
		yybegin(YYINITIAL);
		return symbol(Tokens.SEMICOLON);
	}
	
	// heredoc and nowdoc
	{UnquotedHeredocStarter} {
		beginHeredocNowdoc(yytext(), false, true);
	}
	{QuotedHeredocStarter} {
		beginHeredocNowdoc(yytext(), true, true);
	}
	{NowdocStarter} {
		beginHeredocNowdoc(yytext(), true, false);
	}
	
	// punctuation (some of these can be operators too)
	"(" {
		return symbol(Tokens.OPENING_PARENTHESIS);
	}
	")" {
		return symbol(Tokens.CLOSING_PARENTHESIS);
	}
	"{" {
		return symbol(Tokens.OPENING_CURLY_BRACE);
	}
	"}" {
		return symbol(Tokens.CLOSING_CURLY_BRACE);
	}
	"[" {
		return symbol(Tokens.OPENING_SQUARE_BRACKET);
	}
	"]" {
		return symbol(Tokens.CLOSING_SQUARE_BRACKET);
	}
	"->" {
		return symbol(Tokens.RIGHT_ARROW);
	}
	"=>" {
		return symbol(Tokens.DOUBLE_RIGHT_ARROW);
	}
	";" {
		return symbol(Tokens.SEMICOLON);
	}
	"::" {
		return symbol(Tokens.SCOPE_RESOLUTION);
	}
	"?" {
		return symbol(Tokens.QUESTION_MARK);
	}
	":" {
		return symbol(Tokens.COLON);
	}
	"," {
		return symbol(Tokens.COMMA);
	}
	
	// compilation object keywords
	"function" {
		return symbol(Tokens.FUNCTION);
	}
	"class" {
		return symbol(Tokens.CLASS);
	}
	"interface" {
		return symbol(Tokens.INTERFACE);
	}
	"extends" {
		return symbol(Tokens.EXTENDS);
	}
	"implements" {
		return symbol(Tokens.IMPLEMENTS);
	}
	"const" {
		return symbol(Tokens.CONST);
	}
	
	// modifier keywords
	"public" {
		return symbol(Tokens.PUBLIC);
	}
	"protected" {
		return symbol(Tokens.PROTECTED);
	}
	"private" {
		return symbol(Tokens.PRIVATE);
	}
	"abstract" {
		return symbol(Tokens.ABSTRACT);
	}
	"final" {
		return symbol(Tokens.FINAL);
	}
	"static" {
		return symbol(Tokens.STATIC);
	}
	"var" {
		return symbol(Tokens.VAR);
	}
	
	// statement keywords
	"for" {
		return symbol(Tokens.FOR);
	}
	"foreach" {
		return symbol(Tokens.FOREACH);
	}
	"as" {
		return symbol(Tokens.AS);
	}
	"do" {
		return symbol(Tokens.DO);
	}
	"while" {
		return symbol(Tokens.WHILE);
	}
	"break" {
		return symbol(Tokens.BREAK);
	}
	"if" {
		return symbol(Tokens.IF);
	}
	"elseif" {
		return symbol(Tokens.ELSEIF);
	}
	"else" {
		return symbol(Tokens.ELSE);
	}
	"return" {
		return symbol(Tokens.RETURN);
	}
	"throw" {
		return symbol(Tokens.THROW);
	}
	"try" {
		return symbol(Tokens.TRY);
	}
	"catch" {
		return symbol(Tokens.CATCH);
	}
	"finally" {
		return symbol(Tokens.FINALLY);
	}
	"switch" {
		return symbol(Tokens.SWITCH);
	}
	"case" {
		return symbol(Tokens.CASE);
	}
	"default" {
		return symbol(Tokens.DEFAULT);
	}
	"global" {
		return symbol(Tokens.GLOBAL);
	}
	"echo" {
		return symbol(Tokens.ECHO);
	}
	
	// computation and logical operators
	"+" {
		return symbol(Tokens.PLUS);
	}
	"-" {
		return symbol(Tokens.MINUS);
	}
	"*" {
		return symbol(Tokens.TIMES);
	}
	"/" {
		return symbol(Tokens.DIVIDE);
	}
	"%" {
		return symbol(Tokens.MOD);
	}
	"." {
		return symbol(Tokens.CONCAT);
	}
	"**" {
		return symbol(Tokens.POWER);
	}
	"!" {
		return symbol(Tokens.LOGICAL_NOT);
	}
	"&&" {
		return symbol(Tokens.LOGICAL_SHORTCUT_AND);
	}
	"||" {
		return symbol(Tokens.LOGICAL_SHORTCUT_OR);
	}
	"and" {
		return symbol(Tokens.LOW_PRECEDENCE_LOGICAL_SHORTCUT_AND);
	}
	"or" {
		return symbol(Tokens.LOW_PRECEDENCE_LOGICAL_SHORTCUT_OR);
	}
	"xor" {
		return symbol(Tokens.LOW_PRECEDENCE_LOGICAL_XOR);
	}
	"~" {
		return symbol(Tokens.BITWISE_NOT);
	}
	"&" {
		return symbol(Tokens.BITWISE_AND);
	}
	"|" {
		return symbol(Tokens.BITWISE_OR);
	}
	"^" {
		return symbol(Tokens.BITWISE_XOR);
	}
	"<<" {
		return symbol(Tokens.SHIFT_LEFT);
	}
	">>" {
		return symbol(Tokens.SHIFT_RIGHT);
	}
	
	// comparison operators
	"==" {
		return symbol(Tokens.EQUAL);
	}
	"!=" {
		return symbol(Tokens.NOT_EQUAL);
	}
	"<>" {
		return symbol(Tokens.NOT_EQUAL);
	}
	"===" {
		return symbol(Tokens.IDENTICAL);
	}
	"!==" {
		return symbol(Tokens.NOT_IDENTICAL);
	}
	">" {
		return symbol(Tokens.GREATER);
	}
	"<" {
		return symbol(Tokens.LESS);
	}
	">=" {
		return symbol(Tokens.GREATER_EQUAL);
	}
	"<=" {
		return symbol(Tokens.LESS_EQUAL);
	}
	
	// assignment operators
	"=" {
		return symbol(Tokens.ASSIGN);
	}
	"++" {
		return symbol(Tokens.INCREMENT);
	}
	"--" {
		return symbol(Tokens.DECREMENT);
	}
	"+=" {
		return symbol(Tokens.PLUS_ASSIGN);
	}
	"-=" {
		return symbol(Tokens.MINUS_ASSIGN);
	}
	"*=" {
		return symbol(Tokens.TIMES_ASSIGN);
	}
	"/=" {
		return symbol(Tokens.DIVIDE_ASSIGN);
	}
	"%=" {
		return symbol(Tokens.MOD_ASSIGN);
	}
	".=" {
		return symbol(Tokens.CONCAT_ASSIGN);
	}
	"&=" {
		return symbol(Tokens.BITWISE_AND_ASSIGN);
	}
	"|=" {
		return symbol(Tokens.BITWISE_OR_ASSIGN);
	}
	"^=" {
		return symbol(Tokens.BITWISE_XOR_ASSIGN);
	}
	"<<=" {
		return symbol(Tokens.SHIFT_LEFT_ASSIGN);
	}
	">>=" {
		return symbol(Tokens.SHIFT_RIGHT_ASSIGN);
	}
	
	// cast-operator type names. TODO: case-insensitive!
	"int" | "integer" | "long" | "bool" | "boolean" | "float" | "double" | "real" | "string" | "object" {
		return symbol(Tokens.CAST_TYPE_NAME, yytext());
	}
	
	// special expressions and operators
	"@" {
		return symbol(Tokens.SUPPRESS_ERRORS);
	}
	"array" {
		return symbol(Tokens.ARRAY);
	}
	"self" {
		return symbol(Tokens.SELF);
	}
	"parent" {
		return symbol(Tokens.PARENT);
	}
	"new" {
		return symbol(Tokens.NEW);
	}
	"instanceof" {
		return symbol(Tokens.INSTANCEOF);
	}
	"include" {
		return symbol(Tokens.INCLUDE);
	}
	"include_once" {
		return symbol(Tokens.INCLUDE_ONCE);
	}
	"require" {
		return symbol(Tokens.REQUIRE);
	}
	"require_once" {
		return symbol(Tokens.REQUIRE_ONCE);
	}
	"print" {
		return symbol(Tokens.PRINT);
	}
	
	// literals
	"null" {
		return symbol(Tokens.NULL_LITERAL);
	}
	"true" {
		return symbol(Tokens.BOOLEAN_LITERAL, true);
	}
	"false" {
		return symbol(Tokens.BOOLEAN_LITERAL, false);
	}
	{DecimalIntegerLiteral} {
		return symbol(Tokens.INTEGER_LITERAL, parseIntegerLiteral(yytext(), 10));
	}
	{HexadecimalIntegerLiteral} {
		return symbol(Tokens.INTEGER_LITERAL, parseIntegerLiteral(yytext().substring(2), 16));
	}
	{OctalIntegerLiteral} {
		String s = yytext();
		int index1 = s.indexOf('8');
		if (index1 != -1) {
			s = s.substring(0, index1);
		}
		int index2 = s.indexOf('9');
		if (index2 != -1) {
			s = s.substring(0, index2);
		}
		return symbol(Tokens.INTEGER_LITERAL, parseIntegerLiteral(s, 8));
	}
	{BinaryIntegerLiteral} {
		return symbol(Tokens.INTEGER_LITERAL, parseIntegerLiteral(yytext().substring(2), 2));
	}
	{FloatLiteral} {
		return symbol(Tokens.FLOAT_LITERAL, Double.parseDouble(yytext()));
	}
	\' {
		stringBuilder.setLength(0);
		yybegin(SINGLE_QUOTED_STRING);
	}
	\" {
		stringBuilder.setLength(0);
		yybegin(DOUBLE_QUOTED_STRING);
	}

	// primitive expressions (except literals)
	{Identifier} {
		return symbol(Tokens.IDENTIFIER, yytext());
	}
	{LocalVariableSingleIndirection} {
		return symbol(Tokens.LOCAL_VARIABLE_SINGLE_INDIRECTION, yytext().substring(1));
	}
	{LocalVariableMultiIndirection} {
		return symbol(Tokens.LOCAL_VARIABLE_MULTI_INDIRECTION, LocalVariableExpression.parse(yytext()));
	}

	// comments and whitespace
	{Comment} {
		// ignore
	}
	{WhiteSpace} {
		// ignore
	}
  
}

// scanning single-quoted string literals
<SINGLE_QUOTED_STRING> {
	"'" {
		yybegin(CODE); 
		return symbol(Tokens.SINGLE_QUOTED_STRING_LITERAL, stringBuilder.toString());
	}
	"\\'" {
		stringBuilder.append('\'');
	}
	"\\\\" {
		stringBuilder.append('\\');
	}
	[^\\\']+ {
		stringBuilder.append(yytext());
	}
	. {
		stringBuilder.append(yytext());
	}
}

// scanning double-quoted string literals
<DOUBLE_QUOTED_STRING> {
	"\"" {
		yybegin(CODE); 
		return symbol(Tokens.DOUBLE_QUOTED_STRING_LITERAL, stringBuilder.toString());
	}
	"\\\"" {
		stringBuilder.append('\"');
	}
	"\\\\" {
		stringBuilder.append('\\');
	}
	"\\n" {
		stringBuilder.append('\n');
	}
	"\\r" {
		stringBuilder.append('\r');
	}
	"\\t" {
		stringBuilder.append('\t');
	}
	[^\\\"]+ {
		stringBuilder.append(yytext());
	}
	. {
		stringBuilder.append(yytext());
	}
}

<HEREDOC_NOWDOC> {
	{Identifier} / ";"? {LineTerminator} {
		Symbol s = handleHeredocNowdocStopperCandidate(yytext());
		if (s != null) {
			return s;
		}
	}
	{HeredocNowdocContentLine} {
		handleHeredocNowdocContent(yytext());
	}
}

// error fallback
[^] {
	String s = yytext();
	int code = s.charAt(0);
	throw new RuntimeException("Illegal character '" + yytext() + "' (code " + code + ")");
}
