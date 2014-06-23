
// ---------------------------------------------------------------------------------------------------------
// --- header information
// ---------------------------------------------------------------------------------------------------------


// imports
package name.martingeisse.phunky.runtime.parser;
import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.Location;

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
	Location location = new Location(yyline, yycolumn);
	return symbolFactory.newSymbol("EOF", Tokens.EOF, location, location); 
%eofval}
%{

	private ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
	private StringBuilder stringBuilder = new StringBuilder();
	private String heredocNowdocMarker;
	private boolean heredocNowdocVariableInterpolation;

	private Symbol symbol(int type) {
		String text = yytext();
		Location left = new Location(yyline, yycolumn);
		Location right = new Location(yyline, yycolumn + text.length());
		return symbolFactory.newSymbol(text, type, left, right);
	}
  
	private Symbol symbol(int type, Object value) {
		String text = yytext();
		Location left = new Location(yyline, yycolumn);
		Location right = new Location(yyline, yycolumn + text.length());
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
		// TODO interpolation
		yybegin(CODE); 
		return symbol(Tokens.STRING_LITERAL, stringBuilder.toString());
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
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*

// numbers
DecimalIntegerLiteral = 0 | [1-9][0-9]*

// identifiers and keywords
Identifier = [\p{Alpha}\_] [\p{Alnum}\_]*
LocalVariable = "$" {Identifier}

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
	"int" | "integer" | "bool" | "boolean" | "float" | "double" | "real" | "string" | "object" {
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
		return symbol(Tokens.INTEGER_LITERAL, Integer.parseInt(yytext()));
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
	{LocalVariable} {
		return symbol(Tokens.LOCAL_VARIABLE, yytext().substring(1));
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
	\' {
		yybegin(CODE); 
		return symbol(Tokens.STRING_LITERAL, stringBuilder.toString());
	}
	[^\n\r\'\\]+ {
		stringBuilder.append(yytext());
	}
	\\t {
		stringBuilder.append('\t');
	}
	\\n {
		stringBuilder.append('\n');
	}
	\\r {
		stringBuilder.append('\r');
	}
	\\\' {
		stringBuilder.append('\'');
	}
	\\ {
		stringBuilder.append('\\');
	}
}

// scanning double-quoted string literals
<DOUBLE_QUOTED_STRING> {
	\" {
		yybegin(CODE); 
		return symbol(Tokens.STRING_LITERAL, stringBuilder.toString());
	}
	[^\n\r\"\\]+ {
		// TODO interpolate
		stringBuilder.append(yytext());
	}
	\\t {
		stringBuilder.append('\t');
	}
	\\n {
		stringBuilder.append('\n');
	}
	\\r {
		stringBuilder.append('\r');
	}
	\\\" {
		stringBuilder.append('\"');
	}
	\\ {
		stringBuilder.append('\\');
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
